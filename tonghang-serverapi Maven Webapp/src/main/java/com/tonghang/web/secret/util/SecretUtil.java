package com.tonghang.web.secret.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.tonghang.web.common.util.TimeUtil;
import com.tonghang.web.secret.pojo.Secret;
import com.tonghang.web.user.service.UserService;

@Component("secretUtil")
public class SecretUtil {

	
	public Map<String,Object> secretToConverter(Secret secret,boolean all){
		Map<String,Object> secretmap = new HashMap<String, Object>();
		Map<String,Object> result = new HashMap<String, Object>();
		if(secret!=null){
			secretmap.put("client_id", secret.getUser().getClient_id());
			secretmap.put("secret_id", secret.getSecret_id());
			secretmap.put("title", secret.getTitle());
			secretmap.put("created_at", TimeUtil.getFormatString(new Date()));
			if(all)
				secretmap.put("content", secret.getContent());
		}
		result.put("secret", secretmap);
		return result;
	}
	
	public Map<String,Object> secretsToMapConverter(List<Secret> secrets,boolean all){
		Map<String,Object> result = new HashMap<String, Object>();
		List<Map<String,Object>> smap = new ArrayList<Map<String,Object>>();
		for(Secret secret:secrets){
			Map<String,Object> secretmap = new HashMap<String, Object>();
			secretmap.put("client_id", secret.getUser().getClient_id());
			secretmap.put("secret_id", secret.getSecret_id());
			secretmap.put("title", secret.getTitle());
			secretmap.put("created_at", TimeUtil.getFormatString(new Date()));
			if(all)
				secretmap.put("content", secret.getContent());
			smap.add(secretmap);
		}
		result.put("secrets", smap);
		return result;
	}
}
