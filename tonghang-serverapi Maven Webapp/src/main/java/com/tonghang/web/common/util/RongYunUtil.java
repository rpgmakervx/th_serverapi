package com.tonghang.web.common.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentException;
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
		String appid =  Constant.RONGYUN_APPID;
		String token = Constant.RONGYUN_APP_TOKEN;
		String timestamp = TimeUtil.timestamp(new Date());
		String sig = SecurityUtil.getMD5(appid+token+timestamp);
		//配置必要参数
		HttpHeaders header = new HttpHeaders();
		Map<String,Object> parts = new HashMap<String, Object>();
		Map<String,Object> reqparam = null;
		parts.put("name", owner_id);
		parts.put("type", Constant.ROOM_TYPE);
		parts.put("permission", Constant.ROOM_PERMISSION);
		header.add("Authorization", SecurityUtil.getBase64(appid+":"+timestamp));
		header.add("Content-Type","application/xml");
		//组织请求参数和请求头
		String reqxml = XmlUtils.map2xmlBody(parts);
//		try {
//			reqparam = XmlUtils.xmlBody2map(reqxml,"Request");
//		} catch (DocumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		reqparam.put("name", owner_id);
//		reqparam.put("type", Constant.ROOM_TYPE);
//		reqparam.put("permission", Constant.ROOM_PERMISSION);
		HttpEntity<String> requestEntity=
				new HttpEntity<String>(reqxml,header);
		//获取相应参数
		ResponseEntity<Map> response = DataUtil.postXml(Constant.RONGYUN_TEST_URL+"/SubAccounts/"+appid+owner_id+"/Group/CreateGroup?sig="+sig, requestEntity, Map.class);
		Map map = response.getBody();
		System.out.println("response : "+map);
		String groupId = (String)map.get("groupId");
		
		return groupId;
	}
	
	public String createMeeting(String owner_id) throws Exception{
		//准备基础参数
		String appid =  Constant.RONGYUN_APPID;
		String accountsid =  Constant.RONGYUN_ACCOUNT;
		String token = Constant.RONGYUN_APP_TOKEN;
		String account_token = Constant.RONGYUN_TOKEN;
		String timestamp = TimeUtil.timestamp(new Date());
		String sig = SecurityUtil.getMD5(accountsid+account_token+timestamp);
		//配置必要参数
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization", SecurityUtil.getBase64(accountsid+":"+timestamp));
		header.add("Content-Type","application/xml");
		String reqxml = "<?xml version='1.0' encoding='utf-8'?><Request><Appid>"+appid+"</Appid><CreateConf action='createconfresult.jsp' maxmember='300'/></Request>";
		System.out.println("xml param :　"+reqxml);
		HttpEntity<String> requestEntity=
				new HttpEntity<String>(reqxml,header);
		//获取相应参数
		ResponseEntity<Map> response = DataUtil.postXml(Constant.RONGYUN_TEST_URL+"/Accounts/"+accountsid+"/ivr/createconf?sig="+sig, requestEntity, Map.class);
		Map map = response.getBody();
		System.out.println("meeting response : "+map);
		String meeting_id = (String)map.get("confid");
		System.out.println("meeting : "+meeting_id);
		return meeting_id;
	}
}
