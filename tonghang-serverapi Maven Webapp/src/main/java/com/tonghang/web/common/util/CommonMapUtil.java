package com.tonghang.web.common.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("commonUtil")
public class CommonMapUtil {

	public static Map<String,Object> baseMsgToMapConvertor(){
//		Map<String,Object> result = new HashMap<String, Object>();
//		result.put("sys_time", TimeUtil.getFormatString(new Date()));
//		result.put("pic_server", Constant.PICTURE_SERVER);
//		result.put("code", Constant.SUCCESS);
//		result.put("message", "server normal");
		return buildMap(Constant.SUCCESS, "server normal");
	}
	/**
	 * 带有自定义message和code的baseconverter
	 * @param message
	 * @param code
	 * @return
	 */
	public static Map<String,Object> baseMsgToMapConvertor(String message,int code){
		return buildMap(code, message);
	}
	
	private static Map<String,Object> buildMap(int code,String message){
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("sys_time", TimeUtil.getFormatString(new Date()));
		result.put("pic_server", Constant.PICTURE_SERVER);
		result.put("code", code);
		result.put("message", message);
		return result;
	}
//重构部分
	/**
	 * 整合所有的返回前端的结果集
	 * @param usermap
	 * @param basemap
	 * @param result
	 * @return
	 */
	public static void generateResult(Map<String,Object> usermap,Map<String,Object> basemap,Map<String,Object> result){
		if(usermap!=null){
			usermap.putAll(basemap);
			result.put("success", usermap);
		}
		else 
			result.put("success", basemap);
	}
	//解析commonmap
	public static Map<String,Object> decodeCommonMap(Map<String,Object> map){
		Map<String,Object> success = (Map<String, Object>) map.get("success");
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("sys_time", success.get("sys_time"));
		result.put("pic_server", success.get("pic_server"));
		result.put("code", (Integer)success.get("code"));
		result.put("message", success.get("message"));
		return result;
	}
}
