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
import com.tonghang.web.room.pojo.Room;
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
	
	public Map<String,Object> roomToMapConverterTemplate(Room room){
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("room", roomMapBuilder(room, false));
		return result;
	}
	
	public List<Map<String,Object>> roomsToMapConverterTemplate(Collection<Room> rooms){
		List<Map<String,Object>> roomsmsg = new ArrayList<Map<String,Object>>();
		for(Room room:rooms){
			roomsmsg.add(roomMapBuilder(room, false));
		}
		return roomsmsg;
	}
	
	/**
	 * 返回一个room的map格式
	 * @param room
	 * @return
	 */
	public Map<String,Object> roomToMapConverter(Room room){
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("room", roomMapBuilder(room, true));
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
			roomsmsg.add(roomMapBuilder(room, true,me.getClient_id()));
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
			roomsmsg.add(roomMapBuilder(room, true));
		}
		return roomsmsg;
	}
	
	/**
	 * 封装 room 数据的方法
	 * @param room
	 * @return
	 */
	private Map<String,Object> roomMapBuilder(Room room,boolean owner,String... my_id){
		Map<String,Object> roommsg = new HashMap<String, Object>();
		if(room!=null){
			if(owner){
				userRoomMap(roommsg, room);
			}
			if(my_id!=null&&my_id.length!=0){
				distanceUserMap(room.getUser(), my_id[0], roommsg);
			}
			baseRoomMap(roommsg, room);
		}else{
			nullRoomMap(roommsg, room);
		}
		return roommsg;
	}
	
	//基础的封装Room的方法
	private void baseRoomMap(Map<String,Object> roommsg,Room room){
		roommsg.put("room_id", room.getRoom_id());
		roommsg.put("meeting_id", room.getMeeting_id());
		roommsg.put("created_at", TimeUtil.getFormatString(room.getCreated_at()));
		roommsg.put("theme", room.getTheme());
		roommsg.put("member_num", room.getMember_num());
		roommsg.put("online", room.getOnline()==1?true:false);
	}
	//空的room体
	private void nullRoomMap(Map<String,Object> roommsg,Room room){
		roommsg.put("owner","");
		roommsg.put("room_id", "");
		roommsg.put("meeting_id", "");
		roommsg.put("created_at", "");
		roommsg.put("theme", "");
		roommsg.put("member_num", 0);
		roommsg.put("online", false);
	}
	
	//封装房间主播的方法
	private void userRoomMap(Map<String,Object> roommsg,Room room){
		roommsg.put("owner", userUtil.userToMapConvertorTemplate(room.getUser(),room.getUser().getClient_id()).get("user"));
	}
	
	//封装用户间的距离信息
	private void distanceUserMap(User other,String my_id,Map<String,Object> msg){
		Location my_local = locationService.findLocationByUser(userService.findUserById(my_id));
		msg.put("distance", locationService.computeDistance(other, my_local));
	}
	
}
