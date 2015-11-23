package com.tonghang.web.room.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.Constant;
import com.tonghang.web.common.util.RongYunUtil;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.room.cache.RoomCache;
import com.tonghang.web.room.dao.RoomDao;
import com.tonghang.web.room.pojo.Room;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

@Service("roomService")
@Transactional
public class RoomService {

	@Resource(name="roomDao")
	private RoomDao roomDao;
	@Resource(name="userService")
	private UserService userService;
	@Resource(name="ryUtil")
	private RongYunUtil ryUtil;
	@Resource(name="roomCache")
	private RoomCache roomCache;
	/**
	 * 通过id查找room
	 * @param room_id
	 * @return
	 */
	public Room findRoomById(String room_id){
		return roomDao.findRoomById(room_id);
	}
	
	public List<Room> findRoomSortByOpenat(){
		return roomDao.findRoomSortByOpenat();
	}
	public List<Room> findRoomSortByCreatedat(){
		return roomDao.findRoomSortByCreatedat();
	}
	/**
	 * 创建一个房间
	 * @param owner_id
	 * @param theme
	 * @throws Exception 
	 */
	public void createRoom(String owner_id,String theme) throws Exception{
		theme = theme==null?"":theme;
		User user = userService.findUserById(owner_id);
		Set<Label> labels = user.getLabellist();
		Room room = new Room();
		room.setCreated_at(new Date());
		room.setOpen_at(new Date());
		room.setUser(user);
		room.setTheme(theme);
		room.setRoom_id(ryUtil.createChatRoom(owner_id));
		room.setMeeting_id(ryUtil.createMeeting(owner_id));
		roomDao.save(room);
		System.out.println("所有的room: "+roomDao.findRoomById(room.getRoom_id()));;
	}
	
	/**
	 * 获得app参数
	 * @return
	 */
	public Map<String,String> fetchAppParam(){
		return ryUtil.getAppId();
	}
	
	/**
	 * 修改房间信息
	 * @param room
	 */
	public void updateRoom(Room room){
		roomDao.saveOrUpdate(room);
	}
	
	public Map<String,Object> recommendRooms(String client_id,boolean byDistance, int page){
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> success = new HashMap<String, Object>();
		User me = userService.findUserById(client_id);
		List<Map<String,Object>> rooms = roomCache.getRecommendCache(me, byDistance);
		int front = (page-1)*Constant.PAGESIZE;
		//当前页数的尾索引
		int now = page*Constant.PAGESIZE;
		//缓存中数据页数
		int cache_page = (rooms.size()/Constant.PAGESIZE)+1;
		if((rooms==null||rooms.size()==0)&&page==1){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("首页推荐没有结果", 520));
		}else if(rooms==null&&page!=1||page>cache_page){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("搜索不到更多了", 520));
		}else{
			if(page==cache_page){
				success.put("rooms", rooms.subList(front, rooms.size()));
				success.putAll(CommonMapUtil.baseMsgToMapConvertor());
				result.put("success", success);
			}else if(page<cache_page){
				success.put("rooms", rooms.subList(front, now));
				success.putAll(CommonMapUtil.baseMsgToMapConvertor());
				result.put("success", success);
			}
		}
		return result;
	}
	/**
	 * 房间是否存在
	 * @param meeting_id
	 * @return
	 */
	public boolean roomExists(String meeting_id){
		Map<String,Object> room = ryUtil.roomProperties(meeting_id);
		int state = (Integer) room.get("state");
		return state==1?true:false;
	}
}
