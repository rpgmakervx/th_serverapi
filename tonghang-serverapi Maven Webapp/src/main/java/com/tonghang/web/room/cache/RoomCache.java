package com.tonghang.web.room.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.tonghang.web.common.util.RongYunUtil;
import com.tonghang.web.room.pojo.Room;
import com.tonghang.web.room.service.RoomService;
import com.tonghang.web.room.util.RoomUtil;
import com.tonghang.web.user.pojo.User;

@Component("roomCache")
public class RoomCache {

	@Resource(name="roomService")
	private RoomService roomService;
	@Resource(name="ryUtil")
	private RongYunUtil ryUtil;
	@Resource(name="roomUtil")
	private RoomUtil roomUtil;
	
	@Cacheable(value="com.tonghang.web.room.cache.RoomCache.getRecommendCache",key="#me.client_id+#byDistance")
	public List<Map<String,Object>> getRecommendCache(User me,boolean byDistance){
		List<Room> exist_room = new ArrayList<Room>();
		List<Room> notexist_room = new ArrayList<Room>();
		List<Room> allrooms = new ArrayList<Room>();
		List<Room> roomsopen = roomService.findRoomSortByOpenat();
		for(Room r:roomsopen){
			if(roomService.roomExists(r.getMeeting_id())){
				exist_room.add(r);
			}else{
				notexist_room.add(r);
			}
		}
		allrooms.addAll(exist_room);
		allrooms.addAll(notexist_room);
		List<Map<String,Object>> roomsmap = new ArrayList<Map<String,Object>>();
		roomsmap = roomUtil.roomsToMapConverter(allrooms,me,byDistance);
		return roomsmap;
	}
}
