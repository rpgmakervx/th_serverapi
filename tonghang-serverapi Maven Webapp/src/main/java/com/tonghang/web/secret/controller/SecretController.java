package com.tonghang.web.secret.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.StringUtil;
import com.tonghang.web.secret.pojo.Secret;
import com.tonghang.web.secret.service.SecretService;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;
import com.tonghang.web.user.util.UserUtil;

@Controller("secretController")
@RequestMapping("secret")
public class SecretController {

	@Resource(name="secretService")
	private SecretService secretService;
	@Resource(name="userService")
	private UserService userService;
	@Resource(name="userUtil")
	private UserUtil userUtil;
	/**
	 * 创建时间：2015-11-02
	 * 业务功能：用户添加秘密信息
	 * @param mapstr
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="create",method=RequestMethod.POST)
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
	 * 修改时间：2015-11-07  把修改秘密信息 改为 删除秘密信息
	 * 业务功能：删除秘密信息
	 * @param mapstr
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="delete",method=RequestMethod.POST)
	@ResponseBody public ResponseEntity<Map<String,Object>> modifySecret(@RequestParam String mapstr) throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> success = new HashMap<String, Object>();
		String secret_id = (String) map.get("secret_id");
		secretService.deleteSecret(secret_id);
		result.putAll(CommonMapUtil.baseMsgToMapConvertor());
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	
	/**
	 * 创建时间：2015-11-02
	 * 业务功能：请求和某人交换名片 发推送
	 * @param mysecret_id
	 * @param othersecret_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="{mysecret_id}/swap/{othersecret_id}",method=RequestMethod.POST)
	@ResponseBody public ResponseEntity<Map<String,Object>> requestSwapSecret(@RequestParam String mapstr,@PathVariable String mysecret_id,@PathVariable String othersecret_id) throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> success = new HashMap<String, Object>();
		String my_id = (String) map.get("my_id");
		String other_id = (String) map.get("other_id");
		secretService.swapRequest(mysecret_id, othersecret_id,my_id,other_id);
		result.putAll(CommonMapUtil.baseMsgToMapConvertor());
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	
	/**
	 * 创建时间：2015-11-02
	 * 业务功能：同意和某人交换秘密 发推送
	 * @param mysecret_id
	 * @param othersecret_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="{othersecret_id}/swap/{mysecret_id}",method=RequestMethod.POST)
	@ResponseBody public ResponseEntity<Map<String,Object>> agreeSwapSecret(@RequestParam String mapstr,@PathVariable String mysecret_id,@PathVariable String othersecret_id) throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> success = new HashMap<String, Object>();
		String my_id = (String) map.get("my_id");
		String other_id = (String) map.get("other_id");
		secretService.agreeSwap(mysecret_id, othersecret_id,my_id,other_id);
		result.putAll(CommonMapUtil.baseMsgToMapConvertor());
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	/**
	 * 添加时间：2015-11-02
	 * 业务功能：查看某个秘密的 title
	 * @param mapstr
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="check/title",method=RequestMethod.POST)
	@ResponseBody public ResponseEntity<Map<String,Object>> checkSecretTitle(@RequestParam String mapstr)throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> success = new HashMap<String, Object>();
		String secret_id = (String)map.get("secret_id");
		String client_id = (String)map.get("client_id");
		System.out.println("secret_id :"+secret_id);
		System.out.println("client_id: "+client_id);
		User user = userService.findUserById(client_id);
		result.putAll(userUtil.userToMapConvertor(user, client_id));
		result.putAll(secretService.getSecretById(secret_id,false));
		result.putAll(CommonMapUtil.baseMsgToMapConvertor());
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	
	/**
	 * 添加时间：2015-11-02
	 * 业务功能：查看某个秘密（详细）
	 * @param mapstr
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="check",method=RequestMethod.POST)
	@ResponseBody public ResponseEntity<Map<String,Object>> checkSecretContext(@RequestParam String mapstr)throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> success = new HashMap<String, Object>();
		String secret_id = (String)map.get("secret_id");
		String client_id = (String)map.get("client_id");
		User user = userService.findUserById(client_id);
		result.putAll(userUtil.userToMapConvertor(user, client_id));
		result.putAll(secretService.getSecretById(secret_id,true));
		result.putAll(CommonMapUtil.baseMsgToMapConvertor());
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	/**
	 * 添加时间：2015-11-02
	 * 业务功能：查看某用户的全部秘密的title
	 * @param mapstr
	 * @return
	 * @throws Exception check/all
	 */
	@RequestMapping(value="check/user",method=RequestMethod.POST)
	@ResponseBody public ResponseEntity<Map<String,Object>> checkUserSecretContext(@RequestParam String mapstr)throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> success = new HashMap<String, Object>();
		String client_id = (String)map.get("client_id");
		result.putAll(secretService.findSecretsByUser(client_id, false));
		result.putAll(CommonMapUtil.baseMsgToMapConvertor());
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
	
	/**
	 * 创建时间：2015-11-07
	 * 业务功能：查看某用户所有的 详细秘密信息
	 * @param mapstr
	 * @return
	 * @throws Exception
	 * notice: 返回体重包含当前用户的详细信息
	 */
	@RequestMapping(value="check/all",method=RequestMethod.POST)
	@ResponseBody public ResponseEntity<Map<String,Object>> checkSlefSecretContext(@RequestParam String mapstr)throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> success = new HashMap<String, Object>();
		String client_id = (String)map.get("client_id");
		System.out.println("client_id: "+client_id);
		result.putAll(secretService.findSecretsByUser(client_id, true));
		User user = userService.findUserById(client_id);
		result.putAll(userUtil.userToMapConvertor(user, client_id));
		result.putAll(CommonMapUtil.baseMsgToMapConvertor());
		success.put("success", result);
		return new ResponseEntity<Map<String,Object>>(success,HttpStatus.OK);
	}
}
