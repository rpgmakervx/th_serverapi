package com.tonghang.web.common.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("ryUtil")
public class RongYunUtil {

	/**
	 * 客户端获取appid和apptoken用来做自定义帐号免验密登录
	 * @return
	 */
	public Map<String,String> getAppId(){
		Map<String,String> account = new HashMap<String, String>();
		account.put("appid", Constant.RONGYUN_APPID);
		account.put("apptoken", Constant.RONGYUN_APP_TOKEN);
		return account;
	}
	
	/**
	 * 在容联云创建房间
	 * @param owner_id
	 * @return
	 */
	public String createChatRoom(String owner_id){
		//准备基础参数
		String timestamp = TimeUtil.timestamp(new Date());
		String sig = SecurityUtil.getMD5(Constant.RONGYUN_APPID+Constant.RONGYUN_APP_TOKEN+timestamp);
		//配置必要参数
		HttpHeaders header = new HttpHeaders();
		Map<String,Object> parts = new HashMap<String, Object>();
		Map<String,Object> reqparam = null;
		parts.put("name", owner_id);
		parts.put("type", Constant.ROOM_TYPE);
		parts.put("permission", Constant.ROOM_PERMISSION);
		header.add("Authorization", SecurityUtil.getBase64(Constant.RONGYUN_APPID+":"+timestamp));
		header.add("Content-Type","application/xml");
		//组织请求参数和请求头
		String reqxml = XmlUtils.map2xmlBody(parts);
		HttpEntity<String> requestEntity=
				new HttpEntity<String>(reqxml,header);
		//获取相应参数
		ResponseEntity<Map> response = DataUtil.postXml(Constant.RONGYUN_URL+"/SubAccounts/"+Constant.RONGYUN_APPID+owner_id+"/Group/CreateGroup?sig="+sig, requestEntity, Map.class);
		Map map = response.getBody();
		System.out.println("response : "+map);
		String groupId = (String)map.get("groupId");
		
		return groupId;
	}
	/**
	 * 创建语音会议室
	 * @param owner_id
	 * @return
	 * @throws Exception
	 */
	public String createMeeting(String owner_id) throws Exception{
		//准备基础参数
		String timestamp = TimeUtil.timestamp(new Date());
		String sig = SecurityUtil.getMD5(Constant.RONGYUN_ACCOUNT+Constant.RONGYUN_TOKEN+timestamp);
		//配置必要参数
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization", SecurityUtil.getBase64(Constant.RONGYUN_ACCOUNT+":"+timestamp));
		header.add("Content-Type","application/xml");
		String reqxml = "<?xml version='1.0' encoding='utf-8'?><Request><Appid>"+Constant.RONGYUN_APPID+"</Appid><CreateConf action='createconfresult.jsp' shortconf='false' passwd='123' maxmember='300'/></Request>";
		System.out.println("xml param :　"+reqxml);
		HttpEntity<String> requestEntity=
				new HttpEntity<String>(reqxml,header);
		//获取相应参数
		System.out.println("meetin url : \n"+Constant.RONGYUN_URL+"/Accounts/"+Constant.RONGYUN_ACCOUNT+"/ivr/createconf?sig="+sig);;
		ResponseEntity<Map> response = DataUtil.postXml(Constant.RONGYUN_URL+"/Accounts/"+Constant.RONGYUN_ACCOUNT+"/ivr/createconf?sig="+sig, requestEntity, Map.class);
		Map map = response.getBody();
		System.out.println("meeting : "+map);
		String meeting_id = (String)map.get("confid");
		return meeting_id;
	}
	/**
	 * 查看房间是否存在
	 * @param meeting_id
	 * @return
	 */
	public Map<String,Object> roomProperties(String meeting_id){
		Map<String, Object> properties = new HashMap<String, Object>();
		//准备基础参数
		String timestamp = TimeUtil.timestamp(new Date());
		String sig = SecurityUtil.getMD5(Constant.RONGYUN_ACCOUNT+Constant.RONGYUN_TOKEN+timestamp);
		//配置必要参数
		HttpHeaders header = new HttpHeaders();
		System.out.println("Author : \n"+SecurityUtil.getBase64(Constant.RONGYUN_ACCOUNT+":"+timestamp));
		header.add("Authorization", SecurityUtil.getBase64(Constant.RONGYUN_ACCOUNT+":"+timestamp));
		header.add("Content-Type","application/xml");
		String reqxml = "<?xml version='1.0' encoding='utf-8'?><Request><Appid>"+Constant.RONGYUN_APPID+"</Appid><QueryConfState  confid='"+meeting_id+"'/></Request>";
		System.out.println("xml param :　"+reqxml);
		HttpEntity<String> requestEntity=
				new HttpEntity<String>(reqxml,header);
		//获取相应参数
		System.out.println("path\n "+Constant.RONGYUN_URL+"/Accounts/"+Constant.RONGYUN_ACCOUNT+"/ivr/conf?sig="+sig+"&confid="+meeting_id);
		ResponseEntity<Map> response = DataUtil.postXml(Constant.RONGYUN_URL+"/Accounts/"+Constant.RONGYUN_ACCOUNT+"/ivr/conf?sig="+sig+"&confid="+meeting_id, requestEntity, Map.class);
		properties = response.getBody();
		System.out.println("容联云获取房间参数：\n"+properties);
		return properties;
	}
	
	public void findRoom(String owner_id){
		//准备基础参数
		String timestamp = TimeUtil.timestamp(new Date());
		String sig = SecurityUtil.getMD5(Constant.RONGYUN_APPID+Constant.RONGYUN_APP_TOKEN+timestamp);
		//配置必要参数
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization", SecurityUtil.getBase64(Constant.RONGYUN_APPID+":"+timestamp));
		header.add("Content-Type","application/xml");
		//组织请求参数和请求头
		String reqxml = "<Request><groupId>gg8005430136</groupId><name></name></Request>";
		HttpEntity<String> requestEntity=
				new HttpEntity<String>(reqxml,header);
		//获取相应参数
		ResponseEntity<Map> response = DataUtil.postXml(Constant.RONGYUN_URL+"/SubAccounts/"+Constant.RONGYUN_APPID+owner_id+"/Group/SearchPublicGroups?sig="+sig, requestEntity, Map.class);
		Map map = response.getBody();
		System.out.println("response : "+map);
		Map<String,Object> groups = (Map<String, Object>) map.get("groups");
		System.out.println("groups content : "+groups);
	}
	
	public void shutup(String meeting_id,String member_id){
		//准备基础参数
		String timestamp = TimeUtil.timestamp(new Date());
		String sig = SecurityUtil.getMD5(Constant.RONGYUN_ACCOUNT+Constant.RONGYUN_TOKEN+timestamp);
		//配置必要参数
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization", SecurityUtil.getBase64(Constant.RONGYUN_ACCOUNT+":"+timestamp));
		header.add("Content-Type","application/xml");
		String reqxml = "<?xml version='1.0' encoding='utf-8'?><Request><Appid>"+Constant.RONGYUN_APPID+"</Appid><CreateConf callid ='"+meeting_id+"' confid='"+member_id+"' maxmember='300'/></Request>";
		System.out.println("xml param :　"+reqxml);
		HttpEntity<String> requestEntity=
				new HttpEntity<String>(reqxml,header);
		//获取相应参数
		System.out.println("meetin url : \n"+Constant.RONGYUN_URL+"/Accounts/"+Constant.RONGYUN_ACCOUNT+"/ivr/createconf?sig="+sig);;
		ResponseEntity<Map> response = DataUtil.postXml(Constant.RONGYUN_URL+"/Accounts/"+Constant.RONGYUN_ACCOUNT+"/ivr/createconf?sig="+sig, requestEntity, Map.class);
	}
}
