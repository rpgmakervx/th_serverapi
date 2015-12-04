package com.tonghang.web.user.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.common.exception.BaseException;
import com.tonghang.web.common.exception.EmailExistException;
import com.tonghang.web.common.exception.LoginException;
import com.tonghang.web.common.exception.NickNameExistException;
import com.tonghang.web.common.exception.SearchNoResultException;
import com.tonghang.web.common.exception.UpdateUserException;
import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.Constant;
import com.tonghang.web.common.util.EmailUtil;
import com.tonghang.web.common.util.HuanXinUtil;
import com.tonghang.web.common.util.JPushUtil;
import com.tonghang.web.common.util.SMSUtil;
import com.tonghang.web.common.util.SecurityUtil;
import com.tonghang.web.common.util.StringUtil;
import com.tonghang.web.common.util.TimeUtil;
import com.tonghang.web.friend.dao.FriendDao;
import com.tonghang.web.label.dao.LabelDao;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.location.service.LocationService;
import com.tonghang.web.statistics.service.StatisticsService;
import com.tonghang.web.topic.dao.TopicDao;
import com.tonghang.web.topic.pojo.Topic;
import com.tonghang.web.topic.util.TopicUtil;
import com.tonghang.web.user.cache.UserCache;
import com.tonghang.web.user.dao.UserDao;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.util.UserUtil;

@Service("userService")
@Transactional
public class UserService {
	
	@Resource(name="userDao")
	private UserDao userDao;
	@Resource(name="labelDao")
	private LabelDao labelDao;
	@Resource(name="topicDao")
	private TopicDao topicDao;
	@Resource(name="friendDao")
	private FriendDao friendDao;
	@Resource(name="statisticsService")
	private StatisticsService statisticsService;
	@Resource(name="locationService")
	private LocationService locationService;
	@Resource(name="userUtil")
	private UserUtil userUtil;
	@Resource(name="userCache")
	private UserCache cache;
	@Resource(name="smsUtil")
	private SMSUtil sms;
	
	/**
	 * 用户登录
	 * @param 
	 * @return
	 * @throws Exception 
	 * 自己和自己肯定不是好友，调用带ignore参数的userToMapConvertor方法
	 * update：
	 * 修改时间：2015-10-25  取消所有MD5，添加手机登录
	 */
	public Map<String,Object> login(String number,String password,String what) throws BaseException{
		Map<String,Object> result = new HashMap<String, Object>();
		System.out.println("登录时的密码MD5加密后："+SecurityUtil.getMD5(password));
		User user = null;
		if(what.equals(Constant.EMAIL_LOGIN)){
			user = userDao.findUserByEmail(number);
		}else{
			user = userDao.findUserByPhone(number); 
		}
		if(user==null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor(Constant.LOGIN_FAIL+Constant.EMAIL_NOT_EXISTS, 510));
			if(what.equals(Constant.EMAIL_LOGIN))
				result.put("success", CommonMapUtil.baseMsgToMapConvertor(Constant.LOGIN_FAIL+Constant.EMAIL_NOT_EXISTS, 510));
			else
				result.put("success", CommonMapUtil.baseMsgToMapConvertor(Constant.LOGIN_FAIL+Constant.PHONE_NOT_EXISTS, 510));
			return result;
		}else{
			//把没有容联云id的人设置上RYID
			if("".equals(user.getRy_id())||user.getRy_id()==null){
				user.setRy_id(SecurityUtil.getRYID(user.getClient_id()));
			}
			if(user.getStatus().equals("0")){
				result.put("success", CommonMapUtil.baseMsgToMapConvertor(Constant.LOGIN_FAIL+Constant.USER_ISOLATED, 510));
				return result;
			}else{
				if(user.getPassword().equals(password)){
					Map<String,Object> usermap = userUtil.userToMapConvertor(user,false,user.getClient_id());
					usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
					result.put("success", usermap);
					user.setIsonline("1");
					user.setPassword(password);
					user.setLast_login_at(new Date());
					userDao.saveOrUpdate(user);
				}else{
					result.put("success", CommonMapUtil.baseMsgToMapConvertor(Constant.LOGIN_FAIL+Constant.PASSWORD_INVALID, 510));
					return result;
				}
			}
		}
		return result;
	}
	/**
	 * 业务功能：邮箱找回密码
	 * @param 
	 * @return
	 * @throws EmailExistException 
	 * @throws LoginException 
	 * 
	 * 修改时间: 2015-08-28 忘记密码的随机密码进行了MD5加密
	 * 
	 */
	public Map<String,Object> forgetPassword_Email(String email) throws LoginException{
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserByEmail(email);
		if(user==null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("发送失败，该邮箱不存在！", 510));
			return result;
		}else{
			user.setPassword(StringUtil.randomCode(8));
//调修改密码方法
			EmailUtil.sendEmail(user);
			user.setPassword(SecurityUtil.getMD5(user.getPassword()));
			HuanXinUtil.changePassword(user.getPassword(), user.getClient_id());
			userDao.saveOrUpdate(user);
			result.put("success", userUtil.messageToMapConvertor(200, "密码重置请求成功!"));
		}
		return result;
	}
	
	
	/**
	 * 业务功能：用户注册第一步
	 * @param user User对象(新注册的user对象)
	 * @return
	 * 自己和自己肯定不是好友，调用带ignore参数的userToMapConvertor方法
	 * @throws EmailExistException 
	 * 
	 * notice：第一步注册去掉了添加标签
	 * @throws NickNameExistException 
	 * 
	 * 2015-9-17：删除UserCache中的
	 */
	@CacheEvict(value=
		{"com.tonghang.web.user.cache.UserCache.getSearchLabelCache",
		 "com.tonghang.web.user.cache.UserCache.getRecommendCache",
		 "com.tonghang.web.user.cache.UserCache.getSearchNickNameCache"
		},allEntries = true)
	public Map<String,Object> registUser(User user) throws EmailExistException, NickNameExistException{
		Map<String,Object> result = new HashMap<String, Object>();
		
		if(userDao.findUserByNickName(user.getUsername())!=null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("注册失败！该昵称已经被注册", 512));
			return result;
		}else {
			user.setClient_id(SecurityUtil.getUUID());
			user.setRy_id(SecurityUtil.getRYID(user.getClient_id()));
			userDao.save(user);
			HuanXinUtil.registUser(user);
			Map<String,Object> usermap = userUtil.userToMapConvertor(user,false,user.getClient_id());
			usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
			result.put("success", usermap);
		}
		return result;
	}
	
	/**
	 * 查看用户详细信息
	 * @param client_id	前台请求中的参数	表示用户的唯一标识	client_id
	 * @return
	 * @throws SearchNoResultException 
	 */
	public Map<String,Object> checkUserMessage(String client_id) throws SearchNoResultException{
		Map<String,Object> result = new HashMap<String, Object>();
		User user =userDao.findUserById(client_id);
		if(user==null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("未搜索到您想搜索的内容", 520));
			return result;
		}
		Map<String,Object> usermap = userUtil.userToMapConvertor(user,client_id);
		usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
		result.put("success", usermap);
		return result;
	}

	/**
	 * 判断两个人是不是好友
	 * @param my
	 * @param friend
	 * @return
	 */
	public boolean isFriend(String my,String friend){
		return friendDao.isFriend(my, friend);
	}
	
	/**
	 * 首页推荐
	 * @param 
	 * @return
	 * 使用Set<User> userss先进行包装是为了去掉多余的用户，比如java能推出A用户，android也能推出A用户。
	 * 此时需要用Set去除重复项。
	 * @throws SearchNoResultException 
	 * 
	 * 2015-8-11日新加入排序功能，详情请见SortUtil
	 * 2015-8-27日新加入 在标签排序基础上，按照距离排序功能
	 * 2015-9-16 新增缓存功能。将所有查询结果缓存，在缓存结果中进行分页
	 */
	public Map<String, Object> recommend(String client_id,boolean byDistance, int page){
		long begin = System.currentTimeMillis(); 
		Map<String,Object> result = new HashMap<String, Object>();
		//用来覆盖缓存方法中 succss键对应的users列表
		Map<String,Object> success = new HashMap<String, Object>();
		List<Map<String,Object>> users = cache.getRecommendCache(client_id, byDistance);		
		int front = (page-1)*Constant.PAGESIZE;
		//当前页数的尾索引
		int now = page*Constant.PAGESIZE;
		//缓存中数据页数
		int cache_page = (users.size()/Constant.PAGESIZE)+1;
		if((users==null||users.size()==0)&&page==1){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("首页推荐没有结果", 520));
		}else if(users==null&&page!=1||page>cache_page){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("搜索不到更多了", 520));
		}else{
			if(page==cache_page){
				success.put("users", users.subList(front, users.size()));
				success.putAll(CommonMapUtil.baseMsgToMapConvertor());
				result.put("success", success);
			}else if(page<cache_page){
				success.put("users", users.subList(front, now));
				success.putAll(CommonMapUtil.baseMsgToMapConvertor());
				result.put("success", success);
			}
		}
		return result;
	}
	/**
	 * 按标签搜索用户
	 * @param client_id
	 * @param label_name
	 * @param page
	 * @return
	 * @throws SearchNoResultException
	 * 标签搜索是模糊搜索，当搜索不到时，提示前台搜索不到更多，但是第一次搜索不到则提示没有搜索结果。
	 */
	public Map<String, Object> searchLabel(String client_id,String label_name, boolean byDistance,int page){
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> success = new HashMap<String, Object>();
		List<Map<String,Object>> users = cache.getSearchLabelCache(client_id, label_name, byDistance);
		//当page=1时userss.size()为0说明用户一开始就搜不到数据，
		//page>1时userss.size()为0说明用户刷新了数据，但是没有结果了
		int front = (page-1)*Constant.PAGESIZE;
		//当前页数的尾索引
		int now = page*Constant.PAGESIZE;
		//缓存中数据页数
		System.out.println("当前页数："+page);
		int cache_page = (users.size()/Constant.PAGESIZE)+1;
		System.out.println("缓存总页数："+cache_page);
		System.out.println("缓存方法中取出的数据："+users);
		if((users==null||users.size()==0)&&page==1){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("首页推荐没有结果", 520));
		}else if(users==null&&page!=1||page>cache_page){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("搜索不到更多了", 520));
		}else{
			if(page==cache_page){
				success.put("users", users.subList(front, users.size()));
				success.putAll(CommonMapUtil.baseMsgToMapConvertor());
				result.put("success", success);
			}else if(page<cache_page){
				success.put("users", users.subList(front, now));
				success.putAll(CommonMapUtil.baseMsgToMapConvertor());
				result.put("success", success);
			}
		}
		return result;
	}
	
	/**
	 * 按昵称搜索用户
	 * @param client_id
	 * @param username
	 * @param page
	 * @return
	 * @throws SearchNoResultException
	 * 昵称模糊搜索，当搜索不到时，提示前台搜索不到更多，但是第一次搜索不到则提示没有搜索结果。
	 */
	public Map<String, Object> searchNick(String client_id,String username,boolean byDistance, int page){
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> success = new HashMap<String, Object>();
		List<Map<String,Object>> users = cache.getSearchNickNameCache(client_id, username, byDistance, page);
		int front = (page-1)*Constant.PAGESIZE;
		//当前页数的尾索引
		int now = page*Constant.PAGESIZE;
		//缓存中数据页数
		System.out.println("当前页数："+page);
		int cache_page = (users.size()/Constant.PAGESIZE)+1;
		System.out.println("缓存总页数："+cache_page);
		System.out.println("缓存方法中取出的数据："+users);
		if((users==null||users.size()==0)&&page==1){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("首页推荐没有结果", 520));
		}else if(users==null&&page!=1||page>cache_page){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("搜索不到更多了", 520));
		}else{
			if(page==cache_page){
				success.put("users", users.subList(front, users.size()));
				success.putAll(CommonMapUtil.baseMsgToMapConvertor());
				result.put("success", success);
			}else if(page<cache_page){
				success.put("users", users.subList(front, now));
				success.putAll(CommonMapUtil.baseMsgToMapConvertor());
				result.put("success", success);
			}
		}
		return result;
	}

	/**
	 * 更改时间：2015-12-03
	 * 按照id获取某个用户的所有信息
	 * @param obj_id
	 * @param client_id
	 * @return
	 * notice:obj_id可以是client_id或者ry_id.
	 */
	public Map<String, Object> userMessage(String obj_id,String client_id) {
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<String, Object>();
		User user = null;
		user = userDao.findUserByRYID(obj_id);
		if(user==null){
			user = userDao.findUserById(obj_id);
		}
		Map<String,Object> usermap = new HashMap<String, Object>();
		if(user!=null){
			usermap = userUtil.userToMapConvertor(user,client_id);
		}
		usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
		result.put("success", usermap);
		return result;
	}

	/**
	 * 现在所有的error都放到success中，因为前台无法区分sucess和error
	 * @param client_id
	 * @param username
	 * @param sex
	 * @param birth
	 * @param city
	 * @return
	 * @throws LoginException 
	 * @throws UpdateUserException 
	 * @throws NickNameExistException 
	 * 
	 * notice:修改信息变成一个一个信息进行修改，所以这里逐个判断每个信息是不是空
	 * 2015-09-17：修改信息放到删除缓存的步骤中。
	 */
	public Map<String, Object> update(String client_id, String username,
			String sex, String birth, String city,boolean img){
		// TODO Auto-generated method stub
		return cache.evictUpdateCache(birth, city, sex, username, client_id, img);
	}
	/**
	 * 修改密码
	 * @param client_id
	 * @param old_passwd
	 * @param new_passwd
	 * @return
	 * @throws UpdateUserException
	 * 修改密码操作会给环信发送修改请求，环信密码和自己服务器密码不一致会在发信的时候出问题
	 * notice:密码信息不会缓存，所以不需要封装到删除缓存的方法
	 */
	public Map<String, Object> updatePassword(String client_id,String old_passwd, String new_passwd){
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<String, Object>();
		User user = new User();
		user.setClient_id(client_id);
		user = userDao.findUserById(client_id);
		if(user.getPassword().equals(old_passwd)||user.getPassword().equals(SecurityUtil.getMD5(old_passwd))){
			user.setPassword(new_passwd);
			HuanXinUtil.changePassword(user.getPassword(), user.getClient_id());
			userDao.saveOrUpdate(user);
			user = userDao.findUserById(client_id);
			Map<String,Object> usermap = userUtil.userToMapConvertor(user,client_id);
			usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
			result.put("success", usermap);
		}else {
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("修改失败，原密码不正确", 513));
		}
		return result;
	}
	/**
	 * 业务功能：修改标签
	 * @param client_id
	 * @param list
	 * @return
	 * 2015-09-17：修改用户标签放到删除缓存的步骤中。
	 */
	public Map<String, Object> updateLabel(String client_id, List<String> list) {
		// TODO Auto-generated method stub
		return cache.evictUpdateLabelCache(client_id, list);
	}
	/**
	 * 添加时间：2015-10-26
	 * 业务功能：查找一组用户
	 * @param client_ids
	 * @return
	 */
	public Map<String,Object> getUsersListByIds(List<String> client_ids,String client_id){
		List<User> users = new ArrayList<User>();
		for(String id:client_ids){
			users.add(findUserById(id));
		}
		Map<String,Object> usermap = userUtil.usersToMapConvertor(users,client_id);
		return usermap;
	}
	
	/***
	 * 业务功能：查看某用户的话题
	 * @param client_id
	 * @param page
	 * @return
	 * @throws SearchNoResultException
	 */
	public Map<String, Object> userTopic(String client_id, int page) throws SearchNoResultException {
		// TODO Auto-generated method stub
		List<Topic> topics = topicDao.findTopicByUserId(client_id, page);
		Map<String,Object> result = new HashMap<String, Object>();
		if(topics==null||topics.size()==0){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("该用户没有话题", 520));
			return result;
		}
		return TopicUtil.topicsToMapConvertor(topics);
	}
	/**
	 * 通过id查找某用户
	 * @param client_id
	 * @return
	 */
	public User findUserById(String client_id){
		return userDao.findUserById(client_id);
	}
	
	public User findUserByPhone(String phone){
		return userDao.findUserByPhone(phone);
	}
	/**
	 * 2015-08-26日新增
	 * 
	 * 业务功能：新用户推荐给老用户
	 * @param client_id
	 * @return
	 * @throws SearchNoResultException
	 * 
	 * notice:逐个推送可能会有问题，最好一次推荐一群人而不是一群人一个个推
	 */
	public Map<String,Object> newUserRecommendation(String client_id) throws SearchNoResultException{
		User newuser = findUserById(client_id);
		Map<String,Object>  result = new HashMap<String, Object>();
		result.put("success", CommonMapUtil.baseMsgToMapConvertor());
		int index = 1;
		while(true){
			Map<String,Object> olders = recommend(client_id,false, index);
			Map<String,Object> oldersmap = ((Map<String,Object>)olders.get("success"));
			if(!oldersmap.get("code").equals("200"))
				break;
			List<Map<String,Object>> olderlist = (List<Map<String, Object>>) oldersmap.get("users");
			List<String> client_ids = new ArrayList<String>();
			for(Map<String,Object> older:olderlist){
				client_ids.add((String)older.get("client_id"));
			}
			JPushUtil.pushList(client_ids, client_id, newuser.getUsername(),Constant.RECOMMEND_NEWBE,newuser.getUsername()+Constant.NEWBE_MSG);
			index++;
		}
		return result;
	}
	/**
	 * 2015-08-27新增
	 * 业务功能：用户新增地理位置信息
	 * @param client_id		用户client_id
	 * @param x_point		纬度
	 * @param y_point		经度
	 */
	public void saveUsersLocation(String client_id,double x_point,double y_point){
		User user = findUserById(client_id);
		locationService.saveLocation(user, x_point, y_point);
	}
	/**
	 * 业务功能：向share sdk 发送请求校验验证码，将校验信息反馈给客户端。
	 * 校验前先检查手机号是否已被注册，注册过则不能注册。
	 * @param phonenumber	客户端手机号
	 * @return
	 * @throws Exception 
	 */
	public Map<String,Object> validePhone(String phonenumber,String zone,String validecode) throws Exception{
		Map<String,Object>  result = null;
		if(sms.sendSM(phonenumber, zone, validecode)==Constant.SUCCESS){
			result = CommonMapUtil.baseMsgToMapConvertor(Constant.VALIDECODE_SUCCESS,Constant.SUCCESS);
		}else{
			result = CommonMapUtil.baseMsgToMapConvertor(Constant.VALIDECODE_ERROR,Constant.PHONE_VALIDATE_ERROR_CODE);
		}
		return result;
	}
	/**
	 * 业务功能：修改用户的薪资信息，并记录下一次可以修改薪资的时间。
	 * @param client_id
	 * @param salary
	 * @return
	 */
	public Map<String,Object> updateSalary(String client_id,int salary){
		Map<String,Object>  result = CommonMapUtil.baseMsgToMapConvertor();
		User user = userDao.findUserById(client_id);
		user.setSalary(salary);
		//目前限制3个月之内不能再次更改
		Date d = TimeUtil.plusMonth(Constant.MONTH_GAP,TimeUtil.getFormatShortDate(TimeUtil.getFormatStringDate(new Date())));
		user.setNext_change(d);
		System.out.println("下次才能修改的日期为："+d);
		userDao.saveOrUpdate(user);
		return result;
	}
	/**
	 * 业务功能：创建交换薪资请求。
	 * @param self_id		请求发起方ID
	 * @param other_id		请求接收方ID
	 * @return
	 * notice:给接收方发送推送
	 */
	public Map<String,Object> createRequest(String self_id,String other_id){
		Map<String,Object> success = new HashMap<String, Object>();
		User self = findUserById(self_id);
		JPushUtil.push(other_id, self_id, self.getUsername(), Constant.REQUESTSALARY, self.getUsername()+Constant.EXCHANGE_SALARY_MSG);
		success.put("success", CommonMapUtil.baseMsgToMapConvertor());
		return success;
	}
	/**
	 * 业务功能：对方同意交换名片
	 * @param self_id		请求发起方ID
	 * @param other_id		请求接收方ID
	 * @return
	 */
	public Map<String,Object> agreeExchange(String self_id,String other_id){
		Map<String,Object> success = new HashMap<String, Object>();
		User self = findUserById(self_id);
		JPushUtil.push(other_id, self_id, self.getUsername(), Constant.AGREEEXCHANGESALARY,self.getUsername()+Constant.AGREE_SALARY_MSG);
		success.put("success", CommonMapUtil.baseMsgToMapConvertor());
		return success;
	}
	/**
	 * 业务功能：查看薪资概况（比多少人高比多少人低）
	 * @param client_id
	 * @return
	 */
	public Map<String,Object> salarySuvey(String client_id){
		Map<String,Object> result = new HashMap<String, Object>();
		User self = findUserById(client_id);
		List<User> users = userDao.findAllUser();
		int high = 0;
		int low = 0;
		for(User u:users){
			if(self.getSalary()>u.getSalary()){
				low++;
			}else{
				high++;
			}
		}
		result.put("high", high);
		result.put("low", low);
		return result;
	}
	/**
	 * 业务功能：查看某人的薪资，目前用于查看自己的薪资
	 * @param client_id
	 * @return
	 */
	public Map<String,Object> checkSalary(String client_id){
		User user = findUserById(client_id);
		return userUtil.salaryConvertor(user);
	}
	/**
	 * 业务功能：按首页推荐的条件获得User对象
	 * @param client_id
	 * @return
	 */
	public List<User> recommendUsers(User user){
		List<User> users = new ArrayList<User>();
		Set<User> userss = new HashSet<User>();
		List<String> label_names = new ArrayList<String>();
		Set<Label> labels = user.getLabellist();
		for(Label label : labels){
			List<User> us = userDao.findUserByLabel(label.getLabel_name(), 0);
			if(us.contains(user)){
				us.remove(user);
			}
			label_names.add(label.getLabel_name());
			userss.addAll(us);
		}
		users.addAll(userss);
		return users;
	}
	/**
	 * 业务功能：薪资概况预览
	 * @param client_id
	 * @return
	 * noticce:目前返回信息包括所有人的薪资分布 
	 * 			以及 比自己高和比自己低的人的百分比
	 */
	public Map<String,Object> analyzeUserSalary(String client_id){
		Map<String,Object> success = new HashMap<String, Object>();
		List<Integer> data = new ArrayList<Integer>();
		Map<String,Object> distribution = new HashMap<String, Object>();
		List<User> users = userDao.findAllUser();
		//初始化数据列
		for(int i=0;i<=Constant.SALARY_SIZE+1;i++){
			data.add(0);
		}
		//填充数据列，在对应薪资等级的数据列索引的元素基础上+1
		for(User u:users){
			int level = 0;
			if(u.getSalary()<Constant.SALARY_GAP_2)
				level = u.getSalary()/Constant.SALARY_GAP_1;
			else
				level = (u.getSalary()-Constant.SALARY_GAP_2)/Constant.SALARY_GAP_3+10;
			System.out.println(u.getUsername()+": salary : +"+u.getSalary()+":　－－＞"+level);
			if(level<=Constant.SALARY_SIZE){
				data.set(level,(Integer)data.get(level)+1);
			}else{
				data.set(Constant.SALARY_SIZE+1,(Integer)data.get(Constant.SALARY_SIZE+1)+1);
			}
		}
		distribution.put("data", data);
		distribution.putAll(CommonMapUtil.baseMsgToMapConvertor());
		success.put("success", distribution);
		return success;
	}
	/**
	 * 业务功能：修改用户信息
	 * @param user
	 */
	public void updateUser(User user){
		userDao.saveOrUpdate(user);
	}
	/**
	 * 业务功能：生成邮箱验证码，并保存或更新该验证码
	 * @param email
	 * @param client_id
	 * @return
	 */
	public String generateEmailCode(String email,String client_id){
		User user = userDao.findUserByEmail(email);
		if(user!=null){
			return null;
		}
		String code = StringUtil.randomCode(6);
		User me = userDao.findUserById(client_id);
		code = cache.generateValidateCode(client_id,email);
		System.out.println("当前验证码是："+code);
		EmailUtil.sendEmail(email, "尊敬的" + me.getUsername() + "，您好！\n\n"
				+ "您本次操作获取的验证码为：" + code+"\n 请在两分钟内完成相关操作");
		return code;
	}
	/**
	 * 添加时间：2015-10-26
	 * 业务功能：邮箱验证码校验
	 * @param client_id
	 * @param code
	 * @return
	 * notice: 修改时间 2015-10-27
	 * 			目前改为 将验证码存储在缓存中，邮箱缓存时间设定为2分钟
	 */
	public Map<String,Object> validateEmailCode(String client_id,String code,String email){
		Map<String,Object> result = new HashMap<String, Object>();
		String c = cache.generateValidateCode(client_id,email);
		if(c.equals(code)){
			result.put("message", Constant.VALIDECODE_SUCCESS);
			result.put("code", Constant.SUCCESS);
		}else{
			result.put("message", Constant.INVALID_CODE);
			result.put("code", Constant.EMAIL_VALIDATE_ERROR_CODE);
		}
		cache.evictValidateCode(client_id,email);
		return result;
	}
}
