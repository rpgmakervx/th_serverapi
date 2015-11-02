package com.tonghang.web.secret.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.StringUtil;
import com.tonghang.web.secret.pojo.Secret;
import com.tonghang.web.secret.service.SecretService;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

@Controller("secretController")
@RequestMapping("secret")
public class SecretController {

	@Resource(name="secretService")
	private SecretService secretService;
	@Resource(name="userService")
	private UserService userService;
	
	/**
	 * 创建时间：2015-11-02
	 * 业务功能：用户添加秘密信息
	 * @param mapstr
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("create")
	@ResponseBody public ResponseEntity<Map<String,Object>> createSecret(@RequestParam String mapstr) throws Exception {
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> success = new HashMap<String, Object>();
		String client_id = (String) map.get("client_id");
		String secret_id = (String) map.get("secret_id");
		String title = (String) map.get("title");
		String content = (String) map.get("content");
		User user = userService.findUserById(client_id);
		Secret secret = new Secret();
		secret.setContent(content);
		secret.setTitle(title);
		secret.setCreated_at(new Date());
		secret.setSecret_id(StringUtil.hmacSha1(client_id, new Date().getTime()+title));
		secret.setUser(user);
		secretService.createSecret(secret);
		result.putAll(CommonMapUtil.baseMsgToMapConvertor());
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	/**
	 * 创建时间：2015-11-02
	 * 业务功能：修改秘密信息
	 * @param mapstr
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("update")
	@ResponseBody public ResponseEntity<Map<String,Object>> modifySecret(@RequestParam String mapstr) throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> success = new HashMap<String, Object>();
		String secret_id = (String) map.get("secret_id");
		String title = (String) map.get("title");
		String content = (String) map.get("content");
		Secret secret = new Secret();
		secret.setSecret_id(secret_id);
		secret.setTitle(title);
		secret.setContent(content);
		secretService.updateSecret(secret);
		result.putAll(CommonMapUtil.baseMsgToMapConvertor());
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
}
