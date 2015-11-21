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
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		try {
			reqparam = XmlUtils.xmlBody2map(reqxml,"Request");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//获取相应参数
		ResponseEntity<Map> response = DataUtil.postXml(Constant.RONGYUN_TEST_URL+"/SubAccounts/"+appid+owner_id+"/Group/CreateGroup", reqparam, Map.class);
		Map map = response.getBody();
		String groupId = (String) ((Map)map.get("Response")).get("groupId");
		
		return groupId;
	}
	
	public String createMeeting() throws Exception{
		//准备基础参数
		String accoountsid =  Constant.RONGYUN_ACCOUNT;
		String token = Constant.RONGYUN_TOKEN;
		String timestamp = TimeUtil.timestamp(new Date());
		String sig = SecurityUtil.getMD5(accoountsid+token+timestamp);
		//配置必要参数
		HttpHeaders header = new HttpHeaders();
		Map<String,Object> parts = new HashMap<String, Object>();
		Map<String,Object> reqparam = null;
		header.add("Authorization", SecurityUtil.getBase64(accoountsid+":"+timestamp));
		header.add("Content-Type","application/xml");
		String reqxml = "<Request><Appid>111222333444555666777888</Appid><CreateConf action='createconfresult.jsp'/></Request>";
		reqparam = XmlUtils.xmlBody2map(reqxml, "Request");
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		//获取相应参数
		ResponseEntity<Map> response = DataUtil.postXml(Constant.RONGYUN_TEST_URL+"/Accounts/"+accoountsid+"/ivr/createconf?maxmember=300", reqparam, Map.class);
		Map map = response.getBody();
		String meeting_id = (String) ((Map)map.get("Response")).get("confid");
		return meeting_id;
	}
}
