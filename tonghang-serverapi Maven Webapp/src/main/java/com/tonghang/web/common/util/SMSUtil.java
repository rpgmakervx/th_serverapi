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
		String paramStr = "?appkey=" +Constant.SHARE_SDK_APPKEY+
				"&phone="+phonenumber+
				"&zone="+zone+
				"&code="+validecode;
		ResponseEntity<String> response = DataUtil.getEntity(Constant.SHARE_SDK_URL+paramStr, String.class);
		Map map = new ObjectMapper().readValue(response.getBody(), HashMap.class);
		System.out.println("phone url  :  "+Constant.SHARE_SDK_URL+paramStr);
		//		Map map = response.getBody();
		Integer status = (Integer) map.get("status");
		System.out.println("SDK response: "+status);
		if(!status.equals(200)){
			return Constant.ERROR;
		}else return Constant.SUCCESS;
	}
}
