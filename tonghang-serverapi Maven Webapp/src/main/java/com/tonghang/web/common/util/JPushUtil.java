package com.tonghang.web.common.util;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.tonghang.web.room.pojo.Room;
import com.tonghang.web.room.util.RoomUtil;

public class JPushUtil {
	/**
	 * 提问的推送
	 * @param id		client_id
	 * @param name	用户名
	 * @param type	推送类型
	 */
	public static void pushQuestion(String to_id,String from_id,String name,Map<String,Object> question,String type,String message){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type","application/json");
		headers.add("Authorization","Basic " + "ZWI0ZTc5YzRhYjE4MmQ3MjVlYzJmZjE1OmVkMzIxNjdhODY0MWFiMWVlODY1OGIzYQ==");
		Map<String, Object> parts = new HashMap<String, Object>();
		parts.put("platform","all");
		Map<String, Object> map1 = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		list.add(to_id);
		map1.put("alias", list);
		parts.put("audience", map1);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("id", from_id);
		map2.put("name", name);
		map2.putAll(question);
		map2.put("type", type);
		//to_id 测试用 后期可以删除.
		map2.put("to_id", to_id);
		map2.put("message", message);
//		map2.put("image", Constant.IMAGE_PATH+from_id+"/"+Constant.IMAGE_NAME);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("msg_content", map2);
		parts.put("message", map3);
		try {
			ResponseEntity<Map> response = DataUtil.postEntity("https://api.jpush.cn/v3/push", new HttpEntity(parts,headers),Map.class);
//			System.out.println("JPush response : "+DataUtil.postEntity("https://api.jpush.cn/v3/push", new HttpEntity(parts,headers),Map.class));
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}
	/**
	 * 一般的推送函数
	 * @param to_id
	 * @param from_id
	 * @param name
	 * @param type
	 * @param message
	 */
	public static void push(String to_id,String from_id,String name,String type,String message){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type","application/json");
		headers.add("Authorization","Basic " + "ZWI0ZTc5YzRhYjE4MmQ3MjVlYzJmZjE1OmVkMzIxNjdhODY0MWFiMWVlODY1OGIzYQ==");
		Map<String, Object> parts = new HashMap<String, Object>();
		parts.put("platform","all");
		Map<String, Object> map1 = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		list.add(to_id);
		map1.put("alias", list);
		parts.put("audience", map1);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("id", from_id);
		map2.put("name", name);
		map2.put("type", type);
		//to_id 测试用 后期可以删除.
		map2.put("to_id", to_id);
		map2.put("message", message);
		map2.put("image", Constant.IMAGE_PATH+from_id+"/"+Constant.IMAGE_NAME);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("msg_content", map2);
		parts.put("message", map3);
		try {
			DataUtil.postEntity("https://api.jpush.cn/v3/push", new HttpEntity(parts,headers),Map.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}
	/**
	 * 一般的推送  推送一群人
	 * @param to_ids
	 * @param from_id
	 * @param name
	 * @param type
	 * @param message
	 */
	public static void pushList(List<String> to_ids,String from_id,String name,String type,String message){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type","application/json");
		headers.add("Authorization","Basic " + "ZWI0ZTc5YzRhYjE4MmQ3MjVlYzJmZjE1OmVkMzIxNjdhODY0MWFiMWVlODY1OGIzYQ==");
		Map<String, Object> parts = new HashMap<String, Object>();
		parts.put("platform","all");
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("alias", to_ids);
		parts.put("audience", map1);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("id", from_id);
		map2.put("name", name);
		map2.put("type", type);
		map2.put("message", message);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("msg_content", map2);
		parts.put("message", map3);
		try {
			DataUtil.postEntity("https://api.jpush.cn/v3/push", new HttpEntity(parts,headers),Map.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}
	/**
	 * 业务功能：推送秘密交换
	 * @param to_ids
	 * @param from_id
	 * @param from_secret
	 * @param to_secret
	 * @param name
	 * @param type
	 * @param message
	 */
	public static void push(String to_id,String from_id,String my_secret,String other_secret,String name,String type,String message){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type","application/json");
		headers.add("Authorization","Basic " + "ZWI0ZTc5YzRhYjE4MmQ3MjVlYzJmZjE1OmVkMzIxNjdhODY0MWFiMWVlODY1OGIzYQ==");
		Map<String, Object> parts = new HashMap<String, Object>();
		parts.put("platform","all");
		Map<String, Object> map1 = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		list.add(to_id);
		map1.put("alias", list);
		parts.put("audience", map1);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("id", from_id);
		map2.put("name", name);
		map2.put("type", type);
		//to_id 测试用 后期可以删除.
		map2.put("to_id", to_id);
		map2.put("my_secret", my_secret);
		map2.put("other_secret", other_secret);
		map2.put("message", message);
		map2.put("image", Constant.IMAGE_PATH+from_id+"/"+Constant.IMAGE_NAME);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("msg_content", map2);
		parts.put("message", map3);
		try {
			DataUtil.postEntity("https://api.jpush.cn/v3/push", new HttpEntity(parts,headers),Map.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}
	/**
	 * 主播上线推送，内含开播时间
	 * @param to_ids
	 * @param from_id
	 * @param name
	 * @param type
	 * @param message
	 */
	public static void pushList(List<String> to_ids,String from_id,String name,Map<String,Object> room,String image, String type,String message){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type","application/json");
		headers.add("Authorization","Basic " + "ZWI0ZTc5YzRhYjE4MmQ3MjVlYzJmZjE1OmVkMzIxNjdhODY0MWFiMWVlODY1OGIzYQ==");
		Map<String, Object> parts = new HashMap<String, Object>();
		parts.put("platform","all");
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("alias", to_ids);
		parts.put("audience", map1);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("id", from_id);
		map2.put("name", name);
		map2.put("room", room);
		map2.put("image", Constant.IMAGE_PATH+room.get("cliennt_id")+File.separator+image+Constant.IMAGE_NAME);
		map2.put("type", type);
		map2.put("message", message);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("msg_content", map2);
		parts.put("message", map3);
		try {
			DataUtil.postEntity("https://api.jpush.cn/v3/push", new HttpEntity(parts,headers),Map.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}
}
