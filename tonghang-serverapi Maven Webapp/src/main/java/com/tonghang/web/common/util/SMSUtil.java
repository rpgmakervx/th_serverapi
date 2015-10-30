package com.tonghang.web.common.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("smsUtil")
public class SMSUtil {

	public int sendSM(String phonenumber,String zone,String validecode) throws Exception {
		Map<String,Object> parts = new HashMap<String, Object>();
		parts.put("appkey", Constant.SHARE_SDK_APPKEY);
		parts.put("zone", zone);
		parts.put("phone", phonenumber);
		parts.put("code", validecode);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts);
		ResponseEntity<String> response = DataUtil.postEntity(Constant.SHARE_SDK_URL, requestEntity, String.class);
		Map map = new ObjectMapper().readValue(response.getBody(), HashMap.class);
		//		Map map = response.getBody();
		Integer status = (Integer) map.get("status");
		System.out.println("SDK response: "+status);
		if(status.equals(468)){
			return Constant.ERROR;
		}else return Constant.SUCCESS;
	}
}
