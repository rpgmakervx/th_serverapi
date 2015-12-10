package com.tonghang.web.room.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.record.pojo.Record;
import com.tonghang.web.record.service.RecordService;
import com.tonghang.web.room.pojo.Room;
import com.tonghang.web.room.service.RoomService;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

@Controller("roomController")
@RequestMapping("room")
public class RoomController {

	@Resource(name="roomService")
	private RoomService roomService;
	@Resource(name="userService")
	private UserService userService;
	@Resource(name="recordService")
	private RecordService recordService;
	
	@RequestMapping("{client_id}/create")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> createRoom(@PathVariable String client_id,@RequestParam String mapstr)throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> success = new HashMap<String, Object>();	
		roomService.createRoom(client_id, (String) map.get("meeting_id"));
		Map<String,Object> result = CommonMapUtil.baseMsgToMapConvertor();
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	
	/**
	 * 添加时间：2015-11-21
	 * 业务功能：用户创建房间
	 * @param mapstr (theme:房间主题，owner_id:房主client_id)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("{room_id}/update")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> modifyRoom(@PathVariable String room_id,@RequestParam String mapstr)throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> success = new HashMap<String, Object>();	
		Map<String,Object> result = new HashMap<String, Object>();
		String theme = (String) map.get("theme");
		Room room = roomService.findRoomById(room_id);
		room.setTheme(theme);
		roomService.updateRoom(room);
		result = CommonMapUtil.baseMsgToMapConvertor();
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	/**
	 * 业务功能：主播进入直播间后更新开播时间
	 * @param room_id
	 * @return
	 */
	@RequestMapping("open/{room_id}")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> openRoom(@PathVariable String room_id){
		Map<String,Object> success = new HashMap<String, Object>();	
		Map<String,Object> result = new HashMap<String, Object>();
		Room room = roomService.findRoomById(room_id);
		room.setOpen_at(new Date());
		room.setOnline(1);
		roomService.updateRoom(room);
		result = CommonMapUtil.baseMsgToMapConvertor();
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	
	@RequestMapping("close/{room_id}")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> closeRoom(@PathVariable String room_id){
		Map<String,Object> success = new HashMap<String, Object>();	
		Map<String,Object> result = new HashMap<String, Object>();
		Room room = roomService.findRoomById(room_id);
		room.setOnline(0);
		roomService.updateRoom(room);
		result = CommonMapUtil.baseMsgToMapConvertor();
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	/**
	 * 创建时间：2015-11-23
	 * 业务功能：首页推荐房间，先按照房间是否有主播排序，再按照房间的最后一次开播时间排序
	 * @param mapstr
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("recommend")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> recommend(@RequestParam String mapstr)throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		String client_id = (String)map.get("client_id");
		System.out.println("recommend param "+(Integer) map.get("pageindex"));
		Map<String,Object> success = roomService.recommendRooms(client_id, 
						(Boolean)map.get("byDistance")==null?false:(Boolean)map.get("byDistance"), 
						(Integer) map.get("pageindex")==null?1:(Integer) map.get("pageindex"));
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	
	/**
	 * 添加时间：2015-11-23
	 * 业务功能：记录用户进出房间
	 * @param mapstr
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("record")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> recordUserInAndOut(@RequestParam String mapstr)throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> success = new HashMap<String, Object>();	
		Map<String,Object> result = new HashMap<String, Object>();
		String room_id = (String) map.get("room_id");
		String client_id = (String) map.get("client_id");
		Room room = roomService.findRoomById(room_id);
		User user = userService.findUserById(client_id);
		Date join_at = (Date)map.get("join_at");
		Date leave_at = (Date)map.get("leave_at");
		Record record = new Record();
		record.setUser(user);
		record.setRoom(room);
		record.setLeave_at(leave_at);
		record.setJoin_at(join_at);
		recordService.recordAction(record);
		result = CommonMapUtil.baseMsgToMapConvertor();
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	
	/**
	 * 添加时间：2015-12-06
	 * 业务功能：客户端定时发送该请求，返回当前房间中的听众数
	 * @param room_id
	 * @param mapstr（byDistance用来清除指定用户的指定缓存,见notice）
	 * @return
	 * @throws Exception
	 * notice:byDistance参数用来清除缓存中指定client_id的指定缓存，因为缓存新建的时候是根据client_id和byDistance来确定的
	 */
	@RequestMapping("member/{room_id}")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> pushRoomMemberNum(@PathVariable String room_id,@RequestParam String mapstr)throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		roomService.changeRoomsMember(room_id, (Integer)map.get("member_num"),(Boolean)map.get("byDistance"));
		Map<String,Object> success = new HashMap<String, Object>();	
		Map<String,Object> result = new HashMap<String, Object>();
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	
	/**
	 * 添加时间2015-11-26
	 * 修改时间：2015-12-06  添加byDistance参数
	 * 业务功能：关注直播间
	 * @param client_id
	 * @param room_id
	 * @param mapstr （byDistance用来清除指定用户的指定缓存,见notice）
	 * @return
	 * @throws Exception 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * notice:byDistance参数用来清除缓存中指定client_id的指定缓存，因为缓存新建的时候是根据client_id和byDistance来确定的
	 */
	@RequestMapping("/{client_id}/follow/{room_id}")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> followRoom(@RequestParam String mapstr,
													@PathVariable String client_id,@PathVariable String room_id) throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> success = new HashMap<String, Object>();	
		Map<String,Object> result = new HashMap<String, Object>();
		roomService.followRoom(room_id, client_id,(Boolean)map.get("byDistance"));
		result = CommonMapUtil.baseMsgToMapConvertor();
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	
	/**
	 * 添加时间：2015-12-10
	 * 业务功能：取消关注该房间
	 * @param mapstr
	 * @param client_id
	 * @param room_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/{client_id}/unfollow/{room_id}")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> unFollowRoom(@RequestParam String mapstr,
													@PathVariable String client_id,@PathVariable String room_id) throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> success = new HashMap<String, Object>();	
		Map<String,Object> result = new HashMap<String, Object>();
		roomService.unFollowRoom(room_id, client_id,(Boolean)map.get("byDistance"));
		result = CommonMapUtil.baseMsgToMapConvertor();
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	
	
}
