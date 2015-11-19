package com.tonghang.web.common.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class RongYunUtil {

	public static String createChatRoom(){
		String accountSid = Constant.RONGYUN_ACCOUNT;
		String token = Constant.RONGYUN_TOKEN;
		String nowtime = TimeUtil.timestamp(new Date());
		String reqxml = "<Appid>"+accountSid+"</Appid><CreateConf action='createconfresult.jsp' maxmember='300'/>";
		Map<String,Object> reqparam = null;
		String timestamp = TimeUtil.timestamp(new Date());
		String sig = SecurityUtil.getMD5(Constant.RONGYUN_ACCOUNT+Constant.RONGYUN_TOKEN+timestamp);
		HttpHeaders header = new HttpHeaders();
		Map<String,Object> parts = new HashMap<String, Object>();
		header.add("Content-Type","application/xml");
		header.add("Authorization", SecurityUtil.getMD5(Constant.RONGYUN_ACCOUNT+":"+timestamp));
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		try {
			reqparam = XmlUtils.xmlBody2map(reqxml,"Request");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataUtil.postEntity(url, reqparam, Map.class);
		return ;
	}
}
