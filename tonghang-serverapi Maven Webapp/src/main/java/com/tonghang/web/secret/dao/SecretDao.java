package com.tonghang.web.secret.dao;

import java.util.List;

import com.tonghang.web.secret.pojo.Secret;

public interface SecretDao {

	//添加一条秘密
	public void save(Secret secret);
	//修改秘密内容
	public void saveOrUpdate(Secret secret);
	
	public Secret findSecretById(String secret_id);
	//查询一个用户的全部秘密信息
	public List<Secret> findSecretByUser(String client_id);
	
	public void delete(Secret secret);
}
