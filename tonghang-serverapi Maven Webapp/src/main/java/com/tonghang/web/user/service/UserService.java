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
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.location.service.LocationService;
import com.tonghang.web.room.pojo.Room;
import com.tonghang.web.statistics.service.StatisticsService;
import com.tonghang.web.topic.dao.TopicDao;
import com.tonghang.web.topic.pojo.Topic;
import com.tonghang.web.topic.util.TopicUtil;
import com.tonghang.web.user.cache.UserCache;
import com.tonghang.web.user.dao.UserDao;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.util.UserUtil;
/**
 * 
 * @author Administrator
 *重构前：710行
 *重构后：
 */
@Service("userService")
@Transactional
public class UserService {
	
	@Resource(name="userDao")
	private UserDao userDao;
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
	 * 通过id查找某用户
	 * @param client_id
	 * @return
	 */
	public User findUserById(String client_id){
		return userDao.findUserById(client_id);
	}
	/**
	 * 通过12位ry_id查找用户
	 * @param ry_id
	 * @return
	 */
	public User findUserByRyId(String ry_id){
		return userDao.findUserByRYID(ry_id);
	}
	/**
	 * 通过手机号查找用户
	 * @param phone
	 * @return
	 */
	public User findUserByPhone(String phone){
		return userDao.findUserByPhone(phone);
	}
	/**
	 * 业务功能：修改用户信息
	 * @param user
	 */
	public void updateUser(User user){
		userDao.saveOrUpdate(user);
	}
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
		//重构函数 accoutNumberExist(number, what); 参数1：账号 参数2：账号类型
		User user = accoutNumberExist(number, what);
		return canLogin(user, password);
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
	 * 修改时间：2015-12-11	该方法已被废弃，目前不需要邮箱找回密码
	 * 
	 */
	@Deprecated
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
		{"com.tonghang.web.user.cache.UserCache.getSearchUserCache"
		},allEntries = true)
	public Map<String,Object> registUser(User user){
		Map<String,Object> result = new HashMap<String, Object>();
		if(userDao.findUserByNickName(user.getUsername())!=null){
			CommonMapUtil.generateResult(null, CommonMapUtil.baseMsgToMapConvertor("注册失败！该昵称已经被注册", 512),result);
		}else {
			user.new UserBuilder().setClient_id(SecurityUtil.getUUID()).setRy_id(SecurityUtil.getRYID(user.getClient_id()));
			userDao.save(user);
			HuanXinUtil.registUser(user);
			CommonMapUtil.generateResult(userUtil.userToMapConvertor(user,user.getClient_id()), CommonMapUtil.baseMsgToMapConvertor(), result);
		}
		return result;
	}
	
	/**
	 * 查看用户详细信息
	 * @param client_id	前台请求中的参数	表示用户的唯一标识	client_id
	 * @return
	 * @throws SearchNoResultException 
	 */
	public Map<String,Object> checkUserMessage(String client_id){
		Map<String,Object> result = new HashMap<String, Object>();
		User user =userDao.findUserById(client_id);
		if(user==null){
			CommonMapUtil.generateResult(null, CommonMapUtil.baseMsgToMapConvertor("未搜索到您想搜索的内容", 520),result);
		}else
			CommonMapUtil.generateResult(userUtil.userToMapConvertor(user,client_id), CommonMapUtil.baseMsgToMapConvertor(), result);
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
		List<Map<String,Object>> users = cache.getRecommendCache(client_id, byDistance);		
		int cache_page = (users.size()/Constant.PAGESIZE)+1;
		return getFindResult(users, page, cache_page);
	}
	/**
	 * 添加时间2015-12-08
	 * 业务功能：整合了按标签搜索和按昵称搜索的功能
	 * @param client_id
	 * @param content	搜索框输入的内容（标签或昵称）
	 * @param byDistance
	 * @param page
	 * @return
	 */
	public Map<String, Object> searchUser(String client_id,String content,boolean byDistance, int page){
		List<Map<String,Object>> users = cache.getSearchUserCache(client_id, content, byDistance);
		System.out.println("搜索条件："+content);
		System.out.println("缓存搜索结果："+users);
		int cache_page = (users.size()/Constant.PAGESIZE)+1;
		return getFindResult(users, page, cache_page);
	}

	/**
	 * 更改时间：2015-12-03
	 * 按照id获取某个用户的所有信息
	 * @param obj_id
	 * @param client_id
	 * @return
	 * notice:obj_id可以是client_id或者ry_id. 由于使用ry_id较多所以先查询ry_id
	 */
	public Map<String, Object> userMessage(String obj_id,String client_id) {
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserByRYID(obj_id);
		if(user==null){
			user = userDao.findUserById(obj_id);
		}
		Map<String,Object> usermap = new HashMap<String, Object>();
		if(user!=null){
			usermap = userUtil.userToMapWithRoomConvertor(user,client_id);
		}
		CommonMapUtil.generateResult(usermap, CommonMapUtil.baseMsgToMapConvertor(), result);
//		usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
//		result.put("success", usermap);
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
	@CacheEvict(value=
		{
		 "com.tonghang.web.user.cache.UserCache.getSearchUserCache",
		 "com.tonghang.web.room.cache.RoomCache.getRecommendCache"
		},allEntries = true)
	public Map<String, Object> update(String client_id, User newuser){
		// TODO Auto-generated method stub
		return cache.evictUpdateCache(client_id,newuser);
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
		User user = userDao.findUserById(client_id);
		if(user.getPassword().equals(old_passwd)){
			user.setPassword(new_passwd);
			HuanXinUtil.changePassword(user.getPassword(), user.getClient_id());
			userDao.saveOrUpdate(user);
			CommonMapUtil.generateResult(userUtil.userToMapConvertor(user,client_id), CommonMapUtil.baseMsgToMapConvertor(), result);
		}else {
			CommonMapUtil.generateResult(null, CommonMapUtil.baseMsgToMapConvertor("修改失败，原密码不正确", 513),result);
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
	@CacheEvict(value=
		{"com.tonghang.web.user.cache.UserCache.getSearchLabelCache",
		 "com.tonghang.web.user.cache.UserCache.getRecommendCache",
		 "com.tonghang.web.user.cache.UserCache.getSearchNickNameCache"
		},allEntries = true)
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
			users.add(findUserByRyId(id));
		}
		return userUtil.usersToMapConvertor(users,client_id);
	}
	
	/***
	 * 业务功能：查看某用户的话题
	 * @param client_id
	 * @param page
	 * @return
	 * @throws SearchNoResultException
	 */
	@Deprecated
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
		pushNewUser(newuser);
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
		if(user!=null)
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
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
	 * 业务功能：生成邮箱验证码，并保存或更新该验证码
	 * @param email
	 * @param client_id
	 * @return
	 */
	public String generateEmailCode(String email,String client_id){
		if(userDao.findUserByEmail(email)!=null){
			return null;
		}
		String code = StringUtil.randomCode(6);
		code = cache.generateValidateCode(client_id,email);
		System.out.println("当前验证码是："+code);
		EmailUtil.sendEmail(email, "尊敬的" + userDao.findUserById(client_id).getUsername() + "，您好！\n\n"
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
		if(cache.generateValidateCode(client_id,email).equals(code)){
			CommonMapUtil.generateResult(null, CommonMapUtil.baseMsgToMapConvertor(Constant.VALIDECODE_SUCCESS, Constant.SUCCESS), result);
		}else{
			CommonMapUtil.generateResult(null, CommonMapUtil.baseMsgToMapConvertor(Constant.INVALID_CODE,Constant.EMAIL_VALIDATE_ERROR_CODE), result);
		}
		cache.evictValidateCode(client_id,email);
		return result;
	}
	
//重构部分
	
	/**
	 * 查看账号是否存在
	 * @param number
	 * @param what
	 * @return
	 * 替换位置：
	 * 			method:login 
	 * 			line number: 79~90
	 */
	private User accoutNumberExist(String number,String what){
		return what.equals(Constant.EMAIL_LOGIN)?userDao.findUserByEmail(number):userDao.findUserByPhone(number);
	}
	
	/**
	 * 检查用户是否可登陆，并放回相应的校验信息
	 * @param user
	 * @param password
	 * @param result
	 * 替换位置：
	 * 			method:login 
	 * 			line number: 81~109
	 * @return 
	 */
	private Map<String, Object> canLogin(User user,String password){
		Map<String,Object> result = new HashMap<String, Object>();
		if(user==null){
			CommonMapUtil.generateResult(null,CommonMapUtil.baseMsgToMapConvertor(Constant.LOGIN_FAIL+Constant.NOUSER, 510),result);
		}else{
			//把没有容联云id的人设置上RYID
			user.getRy_id();
			if(user.isIsolate()){
				CommonMapUtil.generateResult(null,CommonMapUtil.baseMsgToMapConvertor(Constant.LOGIN_FAIL+Constant.USER_ISOLATED, 510),result);
			}else{
				validatePassword(user, password, result);
			}
		}
		return result;
	}
	/**
	 * 校验用户名密码密码
	 * @param user			数据库中的标准用户
	 * @param password		输入的密码
	 * @param result		结果集
	 *  替换位置：
	 * 			method:canLogin 
	 * 			line number: 81~109
	 */
	private void validatePassword(User user,String password,Map<String,Object> result){
		if(user.getPassword().equals(password)){
			CommonMapUtil.generateResult(userUtil.userToMapWithRoomConvertor(user,user.getClient_id()), CommonMapUtil.baseMsgToMapConvertor(),result);
			//User 使用内部类的建造者模式
			user.new UserBuilder().setIsonline("1").setPassword(password).setLast_login_at(new Date());
			userDao.saveOrUpdate(user);
		}else{
			CommonMapUtil.generateResult(null, CommonMapUtil.baseMsgToMapConvertor(Constant.LOGIN_FAIL+Constant.PASSWORD_INVALID, 510),result);
		}
	}
	/**
	 * 从缓存中获取用户数据
	 * @param users
	 * @param page
	 * @param cache_page
	 * @return
	 * 替换位置：
	 * 				method:recommend	line number:226
	 * 				method:searchUser line number:326
	 */
	private Map<String,Object> getFindResult(List<Map<String,Object>> users,int page,int cache_page){
		Map<String,Object> result = new HashMap<String, Object>();
//		Map<String,Object> success = new HashMap<String, Object>();
//		Map<String,Object> usermap = new HashMap<String, Object>();
		if((users==null||users.size()==0)&&page==1){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor(Constant.NO_RESULT, 520));
		}else if(users==null&&page!=1||page>cache_page){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor(Constant.NO_MORE_RESULT, 520));
		}else{
			pagination(users, page, cache_page, result);
		}
		return result;
	}
	//当前页数的尾索引
	private int frontPage(int page){
		return (page-1)*Constant.PAGESIZE;
	}
	//缓存中数据页数
	private int nowPage(int page){
		return page*Constant.PAGESIZE;
	}
	/**
	 * 分页获取users集合中的数据
	 * @param users			要被分页的对象
	 * @param page			当前页数
	 * @param cache_page	缓存的页数
	 * @param result		结果集
	 *
	 * 替换位置
	 * 			method:getFindResult  line number: 788
	 */
	private void pagination(List<Map<String,Object>> users,int page,int cache_page,Map<String,Object> result){
		Map<String,Object> usermap = new HashMap<String, Object>();
		if(page==cache_page){
			usermap.put("users", users.subList(frontPage(page), users.size()));
			CommonMapUtil.generateResult(usermap,CommonMapUtil.baseMsgToMapConvertor(),result);
		}else if(page<cache_page){
			usermap.put("users", users.subList(frontPage(page), nowPage(page)));
			CommonMapUtil.generateResult(usermap,CommonMapUtil.baseMsgToMapConvertor(),result);
		}
	}
	/**
	 * 循环推送新用户
	 * @param client_id		
	 * @param newuser	新用户
	 * 替换部分：	
	 * 				method:newUserRecommendation  line number:499
	 */
	public void pushNewUser(User newuser){
		int index = 1;
		while(true){
			List<Map<String,Object>> olders = userUtil.decodeUsersMap(recommend(newuser.getClient_id(),false, index++));
			if(!(olders.size()==0))
				break;
			List<String> client_ids = new ArrayList<String>();
			for(Map<String,Object> older:olders){
				client_ids.add((String)older.get("client_id"));
			}
			JPushUtil.pushList(client_ids, newuser.getClient_id(), newuser.getUsername(),Constant.RECOMMEND_NEWBE,newuser.getUsername()+Constant.NEWBE_MSG);
		}
	}
	
}
