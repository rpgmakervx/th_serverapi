package com.tonghang.web.secret.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.secret.dao.SecretDao;
import com.tonghang.web.secret.pojo.Secret;
import com.tonghang.web.secret.util.SecretUtil;

@Service("secretService")
@Transactional
public class SecretService {

	@Resource(name="secretDao")
	private SecretDao secretDao;
	@Resource(name="secretUtil")
	private SecretUtil secretUtil;
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
	public void updateSecret(Secret secret){
		Secret s = findSecretById(secret.getSecret_id());
		s.setTitle(secret.getTitle());
		s.setContent(secret.getContent());
		secretDao.saveOrUpdate(s);
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
	 * 业务功能：按秘密的id查找秘密信息
	 * @param secret_id
	 * @return
	 */
	public Secret findSecretById(String secret_id){
		return secretDao.findSecretById(secret_id);
	}
}
