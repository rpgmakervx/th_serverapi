package com.tonghang.web.secret.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.common.util.Constant;
import com.tonghang.web.common.util.JPushUtil;
import com.tonghang.web.secret.dao.SecretDao;
import com.tonghang.web.secret.pojo.Secret;
import com.tonghang.web.secret.util.SecretUtil;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

@Service("secretService")
@Transactional
public class SecretService {

	@Resource(name="secretDao")
	private SecretDao secretDao;
	@Resource(name="secretUtil")
	private SecretUtil secretUtil;
	@Resource(name="userService")
	private UserService userService;
	/**
	 * 业务功能：为某用户添加一个秘密
	 * @param secret
	 */
	public void createSecret(Secret secret){
		secretDao.save(secret);
	}
	
	/**
	 * 业务功能：修改某用户的某个标签
	 * @param secret
	 */
	public void deleteSecret(String secret_id){
		Secret s = findSecretById(secret_id);
		secretDao.delete(s);
	}
	/**
	 * 
	 * @param client_id
	 * @return
	 */
	public Map<String,Object> findSecretsByUser(String client_id,boolean all){
		List<Secret> secrets = secretDao.findSecretByUser(client_id);
		return secretUtil.secretsToMapConverter(secrets, all);
	}
	/**
	 * 业务功能：查询某个秘密的信息
	 * @param secret_id
	 * @param all
	 * @return
	 */
	public Map<String,Object> getSecretById(String secret_id,boolean all){
		return secretUtil.secretToConverter(findSecretById(secret_id), all);
	}
	
	/**
	 * 业务功能：按秘密的id查找秘密信息
	 * @param secret_id
	 * @return
	 */
	public Secret findSecretById(String secret_id){
		return secretDao.findSecretById(secret_id);
	}
	/**
	 * 业务功能：某人请求和另一个人交换秘密
	 * @param mysecret_id		自己秘密的id,
	 * @param othersecret_id
	 */
	public void swapRequest(String mysecret_id,String othersecret_id,String my_id,String other_id){
		User my = userService.findUserById(my_id); 
		User other = userService.findUserById(other_id); 
		JPushUtil.push(other_id, my_id, mysecret_id, othersecret_id, my.getUsername(), Constant.REQUESTSECRET, Constant.REQUESTSECRET_MSG);
	}
	/**
	 * 业务功能：同意某人交换秘密的请求
	 * @param mysecret_id
	 * @param othersecret_id
	 */
	public void agreeSwap(String mysecret_id,String othersecret_id,String my_id,String other_id){
		User my = userService.findUserById(my_id); 
		User other = userService.findUserById(other_id); 
		JPushUtil.push(other_id, my_id, mysecret_id, othersecret_id, my.getUsername(), Constant.AGREEEXCHANGESECRET, Constant.AGREEEXCHANGESECRET_MSG);
	}
}
