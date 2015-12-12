package com.tonghang.web.common.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("commonUtil")
public class CommonMapUtil {

	public static Map<String,Object> baseMsgToMapConvertor(){
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("sys_time", TimeUtil.getFormatString(new Date()));
		result.put("pic_server", Constant.PICTURE_SERVER);
		result.put("code", Constant.SUCCESS);
		result.put("message", "server normal");
		return result;
	}
	
	public static Map<String,Object> baseMsgToMapConvertor(String message,int code){
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("sys_time", TimeUtil.getFormatString(new Date()));
		result.put("pic_server", Constant.PICTURE_SERVER);
		result.put("code", code);
		result.put("message",message);
		if(code!=200)
			System.err.println("抛出自定义异常！！：\n\t"+code+"\n\t"+message);
		return result;
	}
	/**
	 * 整合所有的返回前端的结果集
	 * @param usermap
	 * @param basemap
	 * @param result
	 * @return
	 */
	public static Map<String,Object> generateResult(Map<String,Object> usermap,Map<String,Object> basemap,Map<String,Object> result){
		if(usermap!=null){
			usermap.putAll(basemap);
			result.put("success", usermap);
		}
		else 
			result.put("success", basemap);
		return result;
	}
}
