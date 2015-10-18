package com.tonghang.web.common.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("smsUtil")
public class SMSUtil {

	public String sendSM(String phonenumber,String zone,String validecode){
		Map<String,Object> parts = new HashMap<String, Object>();
		parts.put("appkey", Constant.SHARE_SDK_APPKEY);
		parts.put("zone", zone);
		parts.put("phone", phonenumber);
		parts.put("code", validecode);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts);
		ResponseEntity<Map> response = DataUtil.postEntity(Constant.SHARE_SDK_URL, requestEntity, Map.class);
		Map map = response.getBody();
		String status = (String) map.get("status");
		if(status.equals("468")){
			return Constant.VALIDECODE_ERROR;
		}else return Constant.VALIDECODE_SUCCESS;
	}
}
