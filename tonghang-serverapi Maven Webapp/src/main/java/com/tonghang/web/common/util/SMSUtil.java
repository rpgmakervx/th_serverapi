package com.tonghang.web.common.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component("smsUtil")
public class SMSUtil {

	public void sendSM(String phonenumber){
		Map<String,Object> parts = new HashMap<String, Object>();
		parts.put("account", Constant.CHUANGLAN_USER);
		parts.put("pswd", Constant.CHUANGLAN_PWD);
		parts.put("mobile", phonenumber);
		parts.put("msg", Constant.SM_VALID_MSG);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts);
		DataUtil.templateGetExchange(Constant.CHUANGLAN_URL, requestEntity, Map.class);
	}
}
