package com.tonghang.web.user.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.HuanXinUtil;
import com.tonghang.web.common.util.StringUtil;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.label.service.LabelService;
import com.tonghang.web.user.dao.UserDao;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;
import com.tonghang.web.user.util.UserUtil;

@Component("userCache")
@Transactional
public class UserCache {

	@Resource(name="userUtil")
	private UserUtil userUtil;
	@Resource(name="userDao")
	private UserDao userDao;
	@Resource(name="labelService")
	private LabelService labelService;
	@Resource(name="userService")
	private UserService userService;
	
	/**
	 * 业务功能：缓存UserService类中 getRecommendCache 方法的值，全部缓存。外部通过分页截取部分缓存结果
	 * @param client_id
	 * @param byDistance
	 * @return
	 * notice: value是缓存的名字，key是指定的缓存下的键名
	 */
	@Cacheable(value="com.tonghang.web.user.cache.UserCache.getRecommendCache",key="#client_id+#byDistance")
	public List<Map<String,Object>> getRecommendCache(String client_id,boolean byDistance){
		System.out.println("进入缓存方法：getByPage");
		List<User> users = new ArrayList<User>();
		Set<User> userss = new HashSet<User>();
		List<String> label_names = new ArrayList<String>();
		long begin = System.currentTimeMillis();
		User user = userDao.findUserById(client_id);
		users = userService.recommendUsers(user);
		Map<String,Object> result = byDistance?userUtil.usersToMapSortedWithDistanceConvertor(users, user):userUtil.usersToMapSortedConvertor(users,user);
		Map<String,Object> success = (Map<String, Object>) result.get("success");
		List<Map<String,Object>> us = (List<Map<String, Object>>) success.get("users");
		List<User> usrs = userDao.findOneUserByCreatedAtDesc(0,100);
		for(User u:usrs){
			if(!(users.contains(u)||u.equals(user))){
				Map<String,Object> map = (Map<String, Object>) userUtil.userToMapConvertor(u, client_id).get("user");
				us.add(map);
			}
			if(us.size()>=100){
				break;				
			}
		}
		return us;
	}
	
	/**
	 * 业务功能：缓存UserService类中 searchLabel 方法的值，全部缓存。外部通过分页截取部分缓存结果
	 * @param client_id
	 * @param label_name
	 * @param byDistance
	 * @return
	 */
	@Deprecated
	@Cacheable(value="com.tonghang.web.user.cache.UserCache.getSearchLabelCache",key="#client_id+#byDistance+#label_name")
	public List<Map<String,Object>> getSearchLabelCache(String client_id,String label_name,boolean  byDistance){
		List<Label> labels = labelService.findLabelByName(label_name);
		Set<User> userss = new HashSet<User>(); 
		List<User> users = new ArrayList<User>();
		for(Label label : labels){
			List<User> us = userDao.findUserByLabel(label.getLabel_name(), 0);
			userss.addAll(us);
		}
		users.addAll(userss);
		Map<String,Object> result = byDistance?userUtil.usersToMapSortByDistanceConvertor(users, client_id):userUtil.usersToMapConvertor(users,client_id);
		Map<String,Object> success = (Map<String, Object>) result.get("success");
		List<Map<String,Object>> us = (List<Map<String, Object>>) success.get("users");
		return us;
	}
	@Deprecated
	@Cacheable(value="com.tonghang.web.user.cache.UserCache.getSearchNickNameCache",key="#client_id+#byDistance+#username")
	public List<Map<String,Object>> getSearchNickNameCache(String client_id,String username,boolean byDistance, int page){
		List<User> users = userDao.findUserByUsername(username);
		Map<String,Object> result = byDistance?userUtil.usersToMapSortByDistanceConvertor(users, client_id):userUtil.usersToMapConvertor(users,client_id);
		return userUtil.decodeUsersMap(result);
	}
	
	@Cacheable(value="com.tonghang.web.user.cache.UserCache.getSearchUserCache",key="#client_id+#byDistance+#content")
	public List<Map<String,Object>> getSearchUserCache(String client_id,String content,boolean byDistance){
		//步骤一：查询出姓名模糊匹配的用户
		List<User> users1 = userDao.findUserByUsername(content);
		//步骤二：查询出标签模糊匹配的用户
		List<Label> labels = labelService.findLabelByName(content);
		//临时存放按标签查出来的人
		Set<User> userss = new HashSet<User>(); 
		List<User> users2 = new ArrayList<User>();
		for(Label label : labels){
			List<User> us = userDao.findUserByLabel(label.getLabel_name(), 0);
			userss.addAll(us);
		}
		//整合两个步骤查询出的结果，用set去重
		userss.addAll(users1);
		users2.addAll(userss);
		System.out.println("搜索结果："+users2);
		Map<String,Object> result = byDistance?userUtil.usersToMapSortByDistanceConvertor(users2, client_id):userUtil.usersToMapConvertor(users2,client_id);
		return userUtil.decodeUsersMap(result);
	}
	/**
	 * 业务功能：抽取出来的修改用户信息功能，修改信息时将搜索和首页推荐的缓存数据清空
	 * @param birth
	 * @param city
	 * @param sex
	 * @param username
	 * @param client_id
	 * @return
	 * notice:allEntries表示删除这个缓存的所有键对应的值（即清空缓存）
	 */
	@CacheEvict(value=
		{
		 "com.tonghang.web.user.cache.UserCache.getSearchUserCache",
		 "com.tonghang.web.room.cache.RoomCache.getRecommendCache"
		},allEntries = true)
	public Map<String,Object> evictUpdateCache(String client_id,User newuser){
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserById(client_id);
		if(user==null){
			CommonMapUtil.generateResult(null, CommonMapUtil.baseMsgToMapConvertor("更新失败，当前用户不存在", 513), result);
		}else{
			updateUserMessage(user, newuser, result);
			System.out.println("evictUpdateCache："+result);
		}
		return result;
	}
	
	@CacheEvict(value=
		{"com.tonghang.web.user.cache.UserCache.getSearchUserCache",
		 "com.tonghang.web.room.cache.RoomCache.getRecommendCache"
		},allEntries = true)
	public Map<String,Object> evictUpdateLabelCache(String client_id, List<String> list){
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserById(client_id);
		List<Label> labels = new ArrayList<Label>();
		userDao.deleteAllLabel(client_id);
		user.setLabellist(labelService.makeLabelByLabelnames(list));
		userDao.saveOrUpdate(user);
		CommonMapUtil.generateResult(userUtil.userToMapConvertor(user,client_id), CommonMapUtil.baseMsgToMapConvertor(), result);
		return result;
	}
	/**
	 * 业务功能：缓存中生成验证码
	 * @param client_id
	 * @return
	 */
	@Cacheable(value="com.tonghang.web.user.cache.UserCache.generateValidateCode",key="#client_id+#email")
	public String generateValidateCode(String client_id,String email){
		String code = StringUtil.randomCode(6);
		System.out.println("进入获得验证码并缓存");
		return code;
	}
	@CacheEvict(value={"com.tonghang.web.user.cache.UserCache.generateValidateCode"}
	 	,key = "#client_id+#email")
	public void evictValidateCode(String client_id,String email){ System.out.println("验证码缓存销毁");}

//重构部分
	
	/**
	 * 
	 * @param user
	 * @param newuser
	 * @param result
	 * 替换位置：
	 * 			method: evictUpdateCache    line number: 156
	 */
	private void updateUserMessage(User user,User newuser,Map<String,Object> result){
		User.UserBuilder builder = user.new UserBuilder();
		builder.setBirth(newuser.getBirth()).setCity(newuser.getCity()).setSex(newuser.getSex()).setImage(newuser.getImage());
		if(newuser.getUsername()==null||newuser.getUsername().equals(user.getUsername())){
			CommonMapUtil.generateResult(userUtil.userToMapWithRoomConvertor(user,user.getClient_id()), CommonMapUtil.baseMsgToMapConvertor(),result);
		}else if(userDao.findUserByUsernameUnique(newuser.getUsername()).size()!=0){
			CommonMapUtil.generateResult(null, CommonMapUtil.baseMsgToMapConvertor("该昵称已经被注册!", 512),result);
		}else{
			user.setUsername(newuser.getUsername());
			HuanXinUtil.changeUsername(user.getUsername(),user.getClient_id());				
			CommonMapUtil.generateResult(userUtil.userToMapWithRoomConvertor(user,user.getClient_id()), CommonMapUtil.baseMsgToMapConvertor(),result);
		}	
		userDao.saveOrUpdate(user);
	}
}
