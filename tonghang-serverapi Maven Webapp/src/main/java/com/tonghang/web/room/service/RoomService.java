package com.tonghang.web.room.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.Constant;
import com.tonghang.web.common.util.JPushUtil;
import com.tonghang.web.common.util.RongYunUtil;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.room.cache.RoomCache;
import com.tonghang.web.room.dao.RoomDao;
import com.tonghang.web.room.pojo.Room;
import com.tonghang.web.room.util.RoomUtil;
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
	@Resource(name="roomUtil")
	private RoomUtil roomUtil;
	/**
	 * 通过id查找room
	 * @param room_id
	 * @return
	 */
	public Room findRoomById(String room_id){
		return roomDao.findRoomById(room_id);
	}
	
	public Room findRoomByOwner(String owner_id){
		return roomDao.findRoomByOwner(owner_id);
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
	@CacheEvict(value=
		{"com.tonghang.web.room.cache.RoomCache.getRecommendCache"
		},allEntries = true)
	public void createRoom(String owner_id,String meeting_id) throws Exception{
		User user = userService.findUserById(owner_id);
		System.out.println(owner_id+" user room : "+user);
		if(roomDao.findRoomByOwner(user.getClient_id())==null){
			Room room = new Room().new RoomBuilder().setOnline(1).
					setRoom_id(ryUtil.createChatRoom(user.getRy_id())).setCreated_at(new Date()).
					setRy_id(user.getRy_id()).setMeeting_id(meeting_id).setUser(user).setTheme(Constant.THEME).
					setOpen_at(new Date()).setRoom_id(ryUtil.createChatRoom(user.getRy_id())).build();
//			room.setOnline(1);
//			room.setRy_id(user.getRy_id());
//			room.setCreated_at(new Date());
//			room.setOpen_at(new Date());
//			room.setUser(user);
//			room.setTheme(Constant.THEME);
//			room.setRoom_id(ryUtil.createChatRoom(user.getRy_id()));
//			room.setMeeting_id(meeting_id);
			roomDao.save(room);
		}
	}
	
	/**
	 * 修改房间信息
	 * @param room
	 * 
	 */
	@CacheEvict(value=
		{"com.tonghang.web.room.cache.RoomCache.getRecommendCache"
		},allEntries = true)
	public void updateRoom(Room room){
//		if(!room.equals(roomCache.getRoomState(room.getRoom_id())))
		roomDao.saveOrUpdate(room);
	}
	/**
	 * 按照在线状态和开播时间推荐首页房间
	 * @param client_id
	 * @param byDistance
	 * @param page
	 * @return
	 */
	public Map<String,Object> recommendRooms(String client_id,boolean byDistance, int page){
//		Map<String,Object> result = new HashMap<String, Object>();
//		Map<String,Object> success = new HashMap<String, Object>();
//		System.out.println("recommendRooms Service");
		User me = userService.findUserById(client_id);
		List<Map<String,Object>> rooms = roomCache.getRecommendCache(me, byDistance);
		int cache_page = (rooms.size()/Constant.PAGESIZE)+1;
		return getFindResult(rooms, page, cache_page);
//		System.out.println("首页推荐的房间数量："+rooms);
//		int front = (page-1)*Constant.PAGESIZE;
//		//当前页数的尾索引
//		int now = page*Constant.PAGESIZE;
//		//缓存中数据页数
//		int cache_page = (rooms.size()/Constant.PAGESIZE)+1;
//		if((rooms==null||rooms.size()==0)&&page==1){
//			result.put("success", CommonMapUtil.baseMsgToMapConvertor("首页推荐没有结果", 520));
//		}else if(rooms==null&&page!=1||page>cache_page){
//			result.put("success", CommonMapUtil.baseMsgToMapConvertor("搜索不到更多了", 520));
//		}else{
//			if(page==cache_page){
//				success.put("rooms", rooms.subList(front, rooms.size()));
//				success.putAll(CommonMapUtil.baseMsgToMapConvertor());
//				result.put("success", success);
//			}else if(page<cache_page){
//				success.put("rooms", rooms.subList(front, now));
//				success.putAll(CommonMapUtil.baseMsgToMapConvertor());
//				result.put("success", success);
//			}
//		}
//		return result;
	}
	/**
	 * 房间是否存在
	 * @param meeting_id
	 * @return
	 */
	public boolean roomExists(String meeting_id){
		Room room =  roomDao.findRoomByMeeting(meeting_id);
		return room.getOnline()==1?true:false;
	}
	
	/**
	 * 关注房间直播室
	 * @param room_id
	 */
	@CacheEvict(value=
		{"com.tonghang.web.room.cache.RoomCache.getRecommendCache"
		},key="#client_id+#byDistance")
	public void followRoom(String room_id,String client_id,boolean byDistance){
		Room room = findRoomById(room_id);
		User user = userService.findUserById(client_id);
		if(!room.getFollower().contains(user)){
			room.getFollower().add(user);
		}
		roomDao.saveOrUpdate(room);
	}
	
	/**
	 * 取消关注
	 * @param room_id
	 * @param client_id
	 * @param byDistance
	 */
	@CacheEvict(value=
		{"com.tonghang.web.room.cache.RoomCache.getRecommendCache"
		},key="#client_id+#byDistance")
	public void unFollowRoom(String room_id,String client_id,boolean byDistance){
		Room room = findRoomById(room_id);
		User user = userService.findUserById(client_id);
		if(room.getFollower().contains(user)){
			room.getFollower().remove(user);
		}
		roomDao.saveOrUpdate(room);
	}
	
	/**
	 * 业务功能：修改当前房间中的人数
	 * @param room_id
	 * @param number
	 */
	@CacheEvict(value=
		{"com.tonghang.web.room.cache.RoomCache.getRecommendCache"
		},key="#client_id+#byDistance")
	public void changeRoomsMember(String room_id,int number,boolean byDistance){
		Room room = findRoomById(room_id);
		room.setMember_num(number);
		roomDao.saveOrUpdate(room);
	}
	
	/**
	 * 主播上线推送给关注他的人
	 * @param room
	 */
	public void notifyFollower(Room room){
		List<String> ids = new ArrayList<String>();
		for(User user:room.getFollower()){
			ids.add(user.getClient_id());
		}
		JPushUtil.pushList(ids, room.getUser().getClient_id(), room.getUser().getUsername(), Constant.ANCHOR_ONLINE, Constant.ANCHOR_ONLINE_MSG1+room.getUser().getUsername()+Constant.ANCHOR_ONLINE_MSG2);
	}
	/**
	 * 查看单一直播间,返回直播间内的数据
	 * @param room_id
	 * @return
	 */
	public Map<String,Object> getRoomMessage(String room_id,String client_id){
		return roomUtil.roomToMapConverter(findRoomById(room_id),client_id);
	}
	
	/**
	 * 查看某用户是否已经关注过该房间
	 * @param room
	 * @param member
	 * @return
	 */
	public boolean isFollowed(Room room,User member){
		System.out.println(member.getUsername() +"关注详情："+room);
		if(room==null||member==null)
			return false;
		else return room.getFollower().contains(member);
	}
//重构部分
	
	//当前页数的尾索引
	private int frontPage(int page){
		return (page-1)*Constant.PAGESIZE;
	}
	//缓存中数据页数
	private int nowPage(int page){
		return page*Constant.PAGESIZE;
	}
	/**
	 * 从缓存中获取用户数据
	 * @param users
	 * @param page
	 * @param cache_page
	 * @return
	 */
	private Map<String,Object> getFindResult(List<Map<String,Object>> rooms,int page,int cache_page){
		Map<String,Object> result = new HashMap<String, Object>();
		if((rooms==null||rooms.size()==0)&&page==1){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor(Constant.NO_RESULT, 520));
		}else if(rooms==null&&page!=1||page>cache_page){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor(Constant.NO_MORE_RESULT, 520));
		}else{
			pagination(rooms, page, cache_page, result);
		}
		return result;
	}
	/**
	 * 分页获取users集合中的数据
	 * @param users			要被分页的对象
	 * @param page			当前页数
	 * @param cache_page	缓存的页数
	 * @param result		结果集
	 *  替换位置
	 * 			method:getFindResult
	 */
	private void pagination(List<Map<String,Object>> rooms,int page,int cache_page,Map<String,Object> result){
		Map<String,Object> roommap = new HashMap<String, Object>();
		if(page==cache_page){
			roommap.put("rooms", rooms.subList(frontPage(page), rooms.size()));
			CommonMapUtil.generateResult(roommap,CommonMapUtil.baseMsgToMapConvertor(),result);
		}else if(page<cache_page){
			roommap.put("rooms", rooms.subList(frontPage(page), nowPage(page)));
			CommonMapUtil.generateResult(roommap,CommonMapUtil.baseMsgToMapConvertor(),result);
		}
	}
}
