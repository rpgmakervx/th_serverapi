package com.tonghang.web.room.service;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tonghang.web.common.util.RongYunUtil;
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
	@Resource(name="ryUtil")
	private RongYunUtil ryUtil;
	
	/**
	 * 创建一个房间
	 * @param owner_id
	 * @param theme
	 * @throws Exception 
	 */
	public void createRoom(String owner_id,String theme) throws Exception{
		theme = theme==null?"":theme;
		User user = userService.findUserById(owner_id);
		Room room = new Room();
		room.setCreated_at(new Date());
		room.setUser(user);
		room.setTheme(theme);
		room.setLabels(user.getLabellist());
		room.setRoom_id(ryUtil.createChatRoom(owner_id));
		room.setMeeting_id(ryUtil.createMeeting());
		roomDao.save(room);
	}
	
	/**
	 * 获得app参数
	 * @return
	 */
	public Map<String,String> fetchAppParam(){
		return ryUtil.getAppId();
	}
}
