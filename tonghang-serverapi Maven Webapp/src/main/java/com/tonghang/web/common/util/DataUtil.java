package com.tonghang.web.common.util;


import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * 来自生平的代码，获得各种请求发送方式，返回值为JSON或者void
 * @author Administrator
 *
 */
public class DataUtil {
	
	private final static RestTemplate jsonTemplate = new RestTemplate();
	private final static RestTemplate xmlTemplate = new RestTemplate();
	static {
		jsonTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		jsonTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		xmlTemplate.getMessageConverters().add(new MappingJackson2XmlHttpMessageConverter());
	}
	public static RestTemplate getJsonTemplate(){
		return jsonTemplate;
	}
	private final static RestTemplate simpleTemplate = new RestTemplate();

	public static <T> ResponseEntity<T> postJson(String url, Object data, Class<T> type,  Object... uriVariables){						
		return jsonTemplate.postForEntity(url, data, type, uriVariables);
	}
	public static <T> ResponseEntity<T> postXml(String url, Object data, Class<T> type,  Object... uriVariables){						
		return xmlTemplate.postForEntity(url, data, type, uriVariables);
	}
	
	public static void putEntity(String url, Object data,  Object... uriVariables){						
		 jsonTemplate.put(url, data, uriVariables);
//		 jsonTemplate.exchange(url, HttpMethod.PUT, (HttpEntity)data, Map.class);
	}
	
	public static void deleteEntity(String url,HttpEntity requestEntity){						
		jsonTemplate.delete(url,requestEntity);
	}
	public static <T> ResponseEntity<T> deleteForEntity(String url, HttpEntity<T> requestEntity){						
		return (ResponseEntity<T>) jsonTemplate.exchange(url,HttpMethod.DELETE,requestEntity,Map.class);
	}
	
	public static <T> ResponseEntity<T> getEntity(String url, Class<T> type,  Object... uriVariables){						
		return jsonTemplate.getForEntity(url, type);
	}
	public static <T> ResponseEntity<T> postEntity(String url, Object data, Class<T> type,  Object... uriVariables){	
		return jsonTemplate.postForEntity(url, data, type, uriVariables);
	}
	
	public static <T> ResponseEntity<T> getJson(String url, Class<T> type,  Object... uriVariables){						
		return jsonTemplate.getForEntity(url, type, uriVariables);
	}
	public static <T> ResponseEntity<T> getXml(String url, Class<T> type,  Object... uriVariables){						
		return xmlTemplate.getForEntity(url, type, uriVariables);
	}
	
	public static <T> ResponseEntity<T> getSimpleEntity(String url, Class<T> type, Object... uriVariables){						
		return simpleTemplate.getForEntity(url, type, uriVariables);
	}
	
	public static <T> ResponseEntity<T> templatePostExchange(String url, HttpEntity<Map<String,Object>> requestEntity, Class<T> responseType, Object... uriVariables){
		return (ResponseEntity<T>) jsonTemplate.exchange(url, HttpMethod.POST, requestEntity,responseType,uriVariables);
	}
	public static <T> ResponseEntity<T> templateGetExchange(String url, HttpEntity<T> requestEntity, Class responseType, Object... uriVariables){
		return (ResponseEntity<T>) jsonTemplate.exchange(url, HttpMethod.GET, requestEntity,responseType,uriVariables);
	}
	

}
