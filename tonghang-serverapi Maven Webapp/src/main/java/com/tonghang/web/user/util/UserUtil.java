package com.tonghang.web.user.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.Constant;
import com.tonghang.web.common.util.SortUtil;
import com.tonghang.web.common.util.TimeUtil;
import com.tonghang.web.friend.service.FriendService;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.location.pojo.Location;
import com.tonghang.web.location.service.LocationService;
import com.tonghang.web.room.pojo.Room;
import com.tonghang.web.room.service.RoomService;
import com.tonghang.web.room.util.RoomUtil;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

@Component("userUtil")
public class UserUtil {

	@Resource(name="userService")
	private UserService userService;
	@Resource(name="friendService")
	private FriendService friendService;
	@Resource(name="locationService")
	private LocationService locationService;
	@Resource(name="roomService")
	private RoomService roomService;
	@Resource(name="roomUtil")
	private RoomUtil roomUtil;
	
	/**
	 * User 对象 最基本的封装模板
	 * @param user
	 * @param client_id
	 * @return
	 */
	public Map<String,Object> userToMapConvertorTemplate(User user,String client_id){
		Map<String,Object> msg = new HashMap<String, Object>();
		msg.put("user", userMapBuilder(user, client_id, conditionGenetator()/*false,false,false*/));
		return msg;
	}
	/**
	 * List<User> 对象 最基本的封装模板
	 * @param users
	 * @return
	 */
	public Map<String,Object> usersToMapConvertorTemplate(List<User> users){
		List<Map<String,Object>> usersmsg = new ArrayList<Map<String,Object>>();
		Map<String,Object> usermap = new HashMap<String, Object>();
		if(users==null){
			users = new ArrayList<User>();
		}
		for(User u:users){
			usermap.putAll(userMapBuilder(u,null,conditionGenetator()/*false,false,false*/));
		}
		usermap.put("users", usersmsg);
		return usermap;
	}
	
	/**
	 * 
	 * @param users(要包装的目标对象List)
	 * @param client_id(当前用户的client_id,用来判断List中每一个用户对象和当前用户是不是好友关系，关系用is_friend来表示)
	 * @return
	 *  新加了查看用户是不是给某用户发送了好友申请
	 */
	public Map<String,Object> usersToMapConvertor(List<User> users,String client_id){
		List<Map<String,Object>> usersmsg = new ArrayList<Map<String,Object>>();
		for(User u:users){
			Map<String,Object> msg = new HashMap<String, Object>();
			msg.put("user", userMapBuilder(u,client_id, conditionGenetator("hasRoom","hasDistance")/*false,false,true*/));
			usersmsg.add(msg);
		}
		return generateResult(usersmsg);
	}
	/**
	 * 获取用户详细信息
	 * @param user
	 * @param client_id
	 * @return
	 */
//	public Map<String,Object> userToMapConvertor(User user,String client_id){
//		Map<String,Object> usermap = new HashMap<String, Object>();
//		usermap.put("user", userMapBuilder(user,false,false, client_id));
//		return usermap;
//	}
	
	/**
	 * 和usersToMapConvertor方法功能一样，多一个按照距离排序的功能
	 * @param user
	 * @param client_id
	 * @return
	 */
	public Map<String,Object> usersToMapSortByDistanceConvertor(List<User> users,String client_id){
		List<Map<String,Object>> usersmsg = new ArrayList<Map<String,Object>>();
		for(User u:users){
			Map<String,Object> msg = new HashMap<String, Object>();
			msg.put("user", userMapBuilder(u,client_id, conditionGenetator("hasFriend","hasRoom","hasDistance")/*true,true,true */));
			usersmsg.add(msg);
		}
		//根据距离排序
		usersmsg = SortUtil.sortByDistance(usersmsg);
		return generateResult(usersmsg);
	}
	
	//带有房间信息的封装过的User
	public Map<String,Object> userToMapWithRoomConvertor(User user,String client_id){
		Map<String,Object> usermap = new HashMap<String, Object>();
		usermap.put("user", userMapBuilder(user, client_id, conditionGenetator("hasFriend","hasRoom","hasDistance")/*true,false,true*/));
		return usermap;
	}
	/**
	 * 重载userToMapConvertor方法，ignore表示忽略好友关系（因为有可能已知对方肯定是或者不是好友关系）
	 * 新加了查看用户是不是给某用户发送了好友申请
	 * @param user
	 * @param ignore
	 * @return
	 */
	public Map<String,Object> userToMapConvertor(User user,String client_id){
		Map<String,Object> usermap = new HashMap<String, Object>();
		usermap.put("user", userMapBuilder(user, client_id, conditionGenetator("hasFriend","hasDistance")/*true,false,true*/));
		return usermap;
	}
	/**
	 * 该方法是usersToMapConvertor(List<User> users,String client_id)方法的改良版，
	 * 核心作用相同，该方法多了一层按标签排序
	 * @param users
	 * @return
	 */
	public Map<String,Object> usersToMapSortedConvertor(List<User> users,User me){
		List<Map<String,Object>> usersmsg = new ArrayList<Map<String,Object>>();
		for(User u:users){
			Map<String,Object> msg = new HashMap<String, Object>();
			msg.put("user", userMapBuilder(u, me.getClient_id(), conditionGenetator("hasDistance")/*false,false,true*/));
			usersmsg.add(msg);
			//比较当前用户哪些标签是根据使用者的标签被推出来的
		}
		//排序操作，详细请看 SortUtil 类
		usersmsg = SortUtil.sortByLabelName(usersmsg, me.getLabelnames());
		return generateResult(usersmsg);
	}
	/**
	 * 该方法是usersToMapSortedConvertor(List<User> users,String client_id)方法的改良版，
	 * 核心作用相同，除了有按行业排序，该方法多了一层按距离排序。现根据行业排序，然后根据距离排序
	 * @param users
	 * @return
	 * notice:在推荐的同行中如果有人没有location记录，则为其设置一个默认坐标
	 */
	public Map<String,Object> usersToMapSortedWithDistanceConvertor(List<User> users,User me){
		List<Map<String,Object>> usersmsg = new ArrayList<Map<String,Object>>();
		for(User u:users){
			Map<String,Object> msg = new HashMap<String, Object>();
			msg.put("user", userMapBuilder(u,me.getClient_id(), conditionGenetator("hasFriend","hasRoom","hasDistance")/*true,true,true*/));
			usersmsg.add(msg);
		}
		//排序操作，详细请看 SortUtil 类
		usersmsg = SortUtil.sortByLabelName(usersmsg, me.getLabelnames());
		if(locationService.findLocationByUser(me)!=null)
			usersmsg = SortUtil.sortByDistance(usersmsg);
		return generateResult(usersmsg);
	}
	
	public Map<String,Object> messageToMapConvertor(int code,String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("code",code);
		map.put("message",message);
		return map;
	}
	/**
	 * 
	 *业务功能：标记用户和目标标签列表有哪些是相同的 
	 * @param u
	 * @param label_names
	 * @return
	 */
	private List<String> markLabel(User u,List<String> label_names){
		List<String> labels = new ArrayList<String>();
		for(Label l:u.getLabellist()){
			boolean ismarked = false;
			for(String lab_name:label_names){
				if(l.getLabel_name().equalsIgnoreCase(lab_name)||l.getLabel_name().contains(lab_name)||lab_name.contains(l.getLabel_name())){
					ismarked = true;
				}	
			}
			if(ismarked){	
				labels.add(l.getLabel_name()+"\t\t");
			}else{
				labels.add(l.getLabel_name());							
			}
		}
		return labels;
	}
	/**
	 * 添加时间：2015-10-19
	 * 业务功能：返回带薪资的用户信息，并返回是否可以更改薪资信息
	 * @param user
	 * @param ignore
	 * @param client_id
	 * @return
	 */
	public Map<String,Object> salaryConvertor(User user){
		Map<String,Object> msg = new HashMap<String, Object>();
		Map<String,Object> usermap = new HashMap<String, Object>();
		int nextdate = 0;
		if(user.getNext_change()!=null){
			nextdate = TimeUtil.dateGap(user.getNext_change(), new Date());
		}
		if(nextdate<=0){
			nextdate = 0;
		}
		msg.put("client_id", user.getClient_id());
		msg.put("ry_id", user.getRy_id());
		msg.put("username", user.getUsername());
		msg.put("salary", user.getSalary()+"");
		msg.put("date_gap", nextdate);
		usermap.put("user", msg);
		return usermap;
	}
//重构部分
	/**
	 * 解析UsersMap
	 * @param success
	 * @return
	 */
	public List<Map<String,Object>> decodeUsersMap(Map<String,Object> success){
		return (List<Map<String,Object>>)((Map<String,Object>)success.get("success")).get("users");
	}
	/**
	 * 解析UserMap
	 * @param success
	 * @return
	 */
	public Map<String,Object> decodeUserMap(Map<String,Object> success){
		return (Map<String,Object>)((Map<String,Object>)success.get("success")).get("user");
	}
	
	/**
	 * 通用封装用户数据的方法,
	 * 该方法中的client_id 这个可变参数的有无 决定了这个方法是否要确定使用者和要封装的用户的关系。
	 * @param user		要封装的用户
	 * @param hasRoom		是否需要把用户的房间信息也封装进去
	 * @param client_id	当前的使用者,客户端只填写一个id,多个id则只取第一个id
	 * @return
	 * notice: client_id 用来封装user对象和它的属性的关系，比如好友关系，距离关系等等，
	 * 			如果不需要这些关系可以将client_id设置为null
	 */
	private Map<String,Object> userMapBuilder(User user,String client_id,Map<String,String> condition){
		Map<String,Object> msg = new HashMap<String, Object>();
		if(condition.get("hasFriend")!=null){//hasFriend
			friendUserMap(user, client_id, msg);
		}
		if(condition.get("hasRoom")!=null){//hasRoom
			roomUserMap(user, msg);
		}
		if(condition.get("hasDistance")!=null){//byDistance
			distanceUserMap(user, client_id, msg);
		}
		baseUserMap(user, msg);
		return msg;
	}
	//基础的用户信息封装
	private void baseUserMap(User user,Map<String,Object> msg){
		msg.put("labels",user.getLabelnames());
		msg.put("email", user.getEmail());
		msg.put("sex", user.getSex());
		msg.put("username", user.getUsername());
		msg.put("phone", user.getPhone());
		msg.put("city", user.getCityValue());
		msg.put("client_id", user.getClient_id());
		msg.put("ry_id", user.getRy_id());
		msg.put("image", Constant.IMAGE_PATH+user.getClient_id()+user.getImage()+Constant.IMAGE_NAME);
		msg.put("created_at", user.getCreated_at());
		msg.put("birth", user.getBirth());
		msg.put("type", true);
	}
	
	//封装用户的房间信息
	private void roomUserMap(User user,Map<String,Object> msg){
		Room room = roomService.findRoomByOwner(user.getClient_id());
		msg.putAll(roomUtil.roomToMapConverterTemplate(room));
		msg.put("followed", usersToMapConvertorTemplate(room==null?null:room.getFollower()));
		msg.put("follow", roomUtil.roomsToMapConverterForFollower(user.getFollow()));
	}
	//封装用户之间的好友关系
	private void friendUserMap(User user,String client_id,Map<String,Object> msg){
		boolean is_friend = userService.isFriend(client_id, user.getClient_id());
		msg.put("is_friend", is_friend);
		if(!is_friend)
			msg.put("has_invitation", friendService.isInvited(client_id, user.getClient_id()));
		else msg.put("has_invitation",!is_friend);
	}
	//封装用户间的距离信息
	private void distanceUserMap(User user,String client_id,Map<String,Object> msg){
		Location my_local = locationService.findLocationByUser(userService.findUserById(client_id));
		msg.put("distance", locationService.computeDistance(user, my_local));
	}
	//根据参数个数生成判断条件，索引所在的参数存在，则按照参数设置，不存在默认为
	private Map<String,String> conditionGenetator(String... condition){
		Map<String,String> result = new HashMap<String, String>();
		for(String str:condition){
			result.put(str, str);
		}
		return result;
	}
	//生成结果集（针对list类型数据）
	private Map<String,Object> generateResult(List<Map<String,Object>> usersmsg){
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> usermap = CommonMapUtil.baseMsgToMapConvertor();
		usermap.put("users", usersmsg);
		result.put("success", usermap);
		return result;
	}
}
