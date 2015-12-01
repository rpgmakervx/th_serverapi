package com.tonghang.web.room.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.tonghang.web.common.util.RongYunUtil;
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
	@Resource(name="ryUtil")
	private RongYunUtil ryUtil;
	
	public Map<String,Object> roomToMapConverterTemplate(Room room){
		Map<String,Object> roommsg = new HashMap<String, Object>();
		if(room == null) return null;
		roommsg.put("room_id", room.getRoom_id());
		roommsg.put("meeting_id", room.getMeeting_id());
		roommsg.put("created_at", TimeUtil.getFormatString(room.getCreated_at()));
		roommsg.put("theme", room.getTheme());
//		roommsg.put("listener", ryUtil.roomProperties(room.getMeeting_id()).get("count"));
		roommsg.put("online", room.getOnline()==1?true:false);
		return roommsg;
	}
	
	public List<Map<String,Object>> roomsToMapConverterTemplate(Collection<Room> rooms){
		List<Map<String,Object>> roomsmsg = new ArrayList<Map<String,Object>>();
		for(Room room:rooms){
			Map<String,Object> roommsg = new HashMap<String, Object>();
			roommsg.put("room_id", room.getRoom_id());
			roommsg.put("meeting_id", room.getMeeting_id());
			roommsg.put("created_at", TimeUtil.getFormatString(room.getCreated_at()));
			roommsg.put("theme", room.getTheme());
//			roommsg.put("listener", ryUtil.roomProperties(room.getMeeting_id()).get("count"));
			roommsg.put("online", room.getOnline()==1?true:false);
			roomsmsg.add(roommsg);
		}
		return roomsmsg;
	}
	
	/**
	 * 返回一个room的map格式
	 * @param room
	 * @return
	 */
	public Map<String,Object> roomToMapConverter(Room room){
		Map<String,Object> roommsg = new HashMap<String, Object>();
		roommsg.put("room_id", room.getRoom_id());
		roommsg.put("meeting_id", room.getMeeting_id());
		roommsg.put("created_at", TimeUtil.getFormatString(room.getCreated_at()));
		roommsg.put("owner", userUtil.userToMapConvertor(room.getUser(), true, room.getUser().getClient_id()).get("user"));
		roommsg.put("theme", room.getTheme());
//		roommsg.put("listener", ryUtil.roomProperties(room.getMeeting_id()).get("count"));
		roommsg.put("online", room.getOnline()==1?true:false);
		return roommsg;
	}
	
	public List<Map<String,Object>> roomsToMapConverter(Collection<Room> rooms,User me,boolean byDistance){
		List<Map<String,Object>> roomsmsg = new ArrayList<Map<String,Object>>();
		Location my_local = locationService.findLocationByUser(me);
		for(Room room:rooms){
			Map<String,Object> roommsg = new HashMap<String, Object>();
			Location his_local = locationService.findLocationByUser(room.getUser());
			double distance = 0.0;
			if(my_local!=null)
				distance= locationService.getDistanceByLocation(my_local, his_local);
			roommsg.put("room_id", room.getRoom_id());
			roommsg.put("meeting_id", room.getMeeting_id());
			roommsg.put("created_at", TimeUtil.getFormatString(room.getCreated_at()));
			roommsg.put("owner", userUtil.userToMapConvertor(room.getUser(), true, room.getUser().getClient_id()).get("user"));
			roommsg.put("theme", room.getTheme());
			roommsg.put("online", room.getOnline()==1?true:false);
			roommsg.put("distance", distance);
			roomsmsg.add(roommsg);
		}
		if(byDistance)
			return roomsmsg;
		return roomsmsg;
	}
	
}
