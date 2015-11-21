package com.tonghang.web.room.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.room.service.RoomService;

@Controller("roomController")
@RequestMapping("room")
public class RoomController {

	@Resource(name="roomService")
	private RoomService roomService;
	
	/**
	 * 添加时间：2015-11-21
	 * 业务功能：用户创建房间
	 * @param mapstr (theme:房间主题，owner_id:房主client_id)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("create")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> createRoom(@RequestParam String mapstr)throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> success = new HashMap<String, Object>();	
		Map<String,Object> result = new HashMap<String, Object>();
		String owner_id = (String) map.get("owner_id");
		String theme = (String) map.get("theme");
		roomService.createRoom(owner_id, theme);
		result = CommonMapUtil.baseMsgToMapConvertor();
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
}
