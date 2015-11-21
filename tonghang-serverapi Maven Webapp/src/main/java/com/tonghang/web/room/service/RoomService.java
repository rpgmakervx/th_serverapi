package com.tonghang.web.room.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tonghang.web.room.dao.RoomDao;
import com.tonghang.web.room.pojo.Room;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

@Service("roomServie")
public class RoomService {

	@Resource(name="roomDao")
	private RoomDao roomDao;
	@Resource(name="userService")
	private UserService userService;
	
	public void createRoom(String owner_id,String theme){
		User user = userService.findUserById(owner_id);
		Room room = new Room();
		room.setCreated_at(new Date());
		room.setUser(user);
		room.setTheme(theme);
		room.setLabels(user.getLabellist());
	}
}
