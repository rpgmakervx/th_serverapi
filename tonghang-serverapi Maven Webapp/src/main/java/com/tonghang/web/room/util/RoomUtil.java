package com.tonghang.web.room.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.tonghang.web.common.util.SortUtil;
import com.tonghang.web.common.util.TimeUtil;
import com.tonghang.web.location.pojo.Location;
import com.tonghang.web.location.service.LocationService;
import com.tonghang.web.question.pojo.Question;
import com.tonghang.web.question.service.QuestionService;
import com.tonghang.web.question.util.QuestionUtil;
import com.tonghang.web.room.pojo.Room;
import com.tonghang.web.room.service.RoomService;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;
import com.tonghang.web.user.util.UserUtil;

@Component("roomUtil")
public class RoomUtil {

	@Resource(name="userService")
	private UserService userService;
	@Resource(name="locationService")
	private LocationService locationService;
	@Resource(name="userUtil")
	private UserUtil userUtil;
	@Resource(name="questionService")
	private QuestionService questionService;
	@Resource(name="questionUtil")
	private QuestionUtil questionUtil;
	@Resource(name="roomService")
	private RoomService roomService;
	
	public Map<String,Object> roomToMapConverterTemplate(Room room){
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("room", roomMapBuilder(room,null, conditionGenetator()));
		return result;
	}
	
	public List<Map<String,Object>> roomsToMapConverterTemplate(Collection<Room> rooms){
		List<Map<String,Object>> roomsmsg = new ArrayList<Map<String,Object>>();
		for(Room room:rooms){
			roomsmsg.add(roomMapBuilder(room,room.getUser().getClient_id(), conditionGenetator()));
		}
		return roomsmsg;
	}
	
	/**
	 * 返回一个room的map格式
	 * @param room			使用者查看的房间
	 * @param client_id		使用者的client_id
	 * @return
	 */
	public Map<String,Object> roomToMapConverter(Room room,String client_id){
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("room", roomMapBuilder(room,client_id, conditionGenetator("hasOwner","hasDistance","hasFollow","hasQuestion")));
		return result;
	}
	/**
	 * 封装一组room
	 * @param rooms
	 * @param me
	 * @param byDistance	是否按距离封装排序
	 * @return
	 */
	public List<Map<String,Object>> roomsToMapConverter(Collection<Room> rooms,User me,boolean byDistance){
		List<Map<String,Object>> roomsmsg = new ArrayList<Map<String,Object>>();
		for(Room room:rooms){
			roomsmsg.add(roomMapBuilder(room,me.getClient_id(), conditionGenetator("hasOwner","hasDistance")));
		}
		if(byDistance)
			return SortUtil.sortByDistance(roomsmsg);
		return roomsmsg;
	}
	
	/**
	 * 单独用来封装关注者的方法
	 * @param rooms
	 * @return
	 */
	public List<Map<String,Object>> roomsToMapConverterForFollower(Collection<Room> rooms){
		List<Map<String,Object>> roomsmsg = new ArrayList<Map<String,Object>>();
		for(Room room:rooms){
			roomsmsg.add(roomMapBuilder(room,room.getUser().getClient_id(), conditionGenetator("hasOwner")));
		}
		return roomsmsg;
	}
	
	/**
	 * 封装 room 数据的方法
	 * @param room
	 * @return
	 */
	private Map<String,Object> roomMapBuilder(Room room,String my_id,Map<String,String> condition){
		Map<String,Object> roommsg = new HashMap<String, Object>();
		if(room!=null){
			if(condition.get("hasOwner")!=null)//hasOwner
				userRoomMap(roommsg, room);
			if(condition.get("hasDistance")!=null)//hasDistance
				distanceUserMap(room.getUser(), my_id, roommsg);
			if(condition.get("hasFollow")!=null)//hasFollow
				followRoomMap(roommsg, room, my_id);
			if(condition.get("hasQuestion")!=null)//hasQuestion
				questionRoomMap(roommsg, room);
			baseRoomMap(roommsg, room);
		}else{
			nullRoomMap(roommsg);
		}
		return roommsg;
	}
	
	//基础的封装Room的方法
	private void baseRoomMap(Map<String,Object> roommsg,Room room){
		roommsg.put("room_id", room.getRoom_id());
		roommsg.put("client_id", room.getUser().getClient_id());
		roommsg.put("meeting_id", room.getMeeting_id());
		roommsg.put("created_at", TimeUtil.getFormatString(room.getCreated_at()));
		roommsg.put("open_at", TimeUtil.getFormatString(room.getOpen_at()));
		roommsg.put("theme", room.getTheme());
		roommsg.put("member_num", room.getMember_num());
		roommsg.put("online", room.getOnline()==1?true:false);
	}
	//空的room体
	private void nullRoomMap(Map<String,Object> roommsg){
		roommsg.put("owner","");
		roommsg.put("room_id", "");
		roommsg.put("client_id", "");
		roommsg.put("meeting_id", "");
		roommsg.put("created_at", "");
		roommsg.put("open_at", "");
		roommsg.put("theme", "");
		roommsg.put("member_num", 0);
		roommsg.put("online", false);
	}
	
	//封装房间主播的方法
	private void userRoomMap(Map<String,Object> roommsg,Room room){
		roommsg.put("owner", userUtil.userToMapConvertorTemplate(room.getUser(),room.getUser().getClient_id()).get("user"));
	}
	//封装房间中的所有问题列表的方法
	private void questionRoomMap(Map<String,Object> roommsg,Room room){
		List<Question> qs = questionService.findQuestionByAnchor(room.getUser().getClient_id());
		roommsg.putAll(questionUtil.questionsToMapConterter(qs));
	}
	
	//封装用户间的距离信息
	private void distanceUserMap(User other,String my_id,Map<String,Object> msg){
		Location my_local = locationService.findLocationByUser(userService.findUserById(my_id));
		msg.put("distance", locationService.computeDistance(other, my_local));
	}
	//封装关注消息的方法
	public void followRoomMap(Map<String,Object> roommsg,Room room,String client_id){
		System.out.println("follow id : searcher: "+client_id);
		roommsg.put("follow_status", roomService.isFollowed(room, userService.findUserById(client_id)));
		//这里的用户列表不叫users,叫followed
		roommsg.put("followed", userUtil.usersToMapConvertorTemplate(room==null?null:room.getFollower()).get("users"));
		//这里一定要用room的信息查询，因为要判断的是room的房主和clint_id所有者的关系
		User anchor = userService.findUserById(room==null?"":room.getUser().getClient_id());
		roommsg.put("follow", roomsToMapConverterForFollower(anchor==null?new ArrayList<Room>():anchor.getFollow()));
//		Room room = roomService.findRoomByOwner(user.getClient_id());
//		msg.putAll(roomUtil.roomToMapConverterTemplate(room));
//		msg.put("followed", usersToMapConvertorTemplate(room==null?null:room.getFollower()));
	}
	//根据参数个数生成判断条件，索引所在的参数存在，则按照参数设置，不存在默认为false
	private Map<String,String> conditionGenetator(String... condition){
		Map<String,String> result = new HashMap<String, String>();
		for(String str:condition){
			result.put(str, str);
		}
		return result;
	}
	
}
