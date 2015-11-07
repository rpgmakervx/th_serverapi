package com.tonghang.web.secret.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.tonghang.web.secret.dao.SecretDao;
import com.tonghang.web.secret.pojo.Secret;

@Repository("secretDao")
public class SecretDaoImpl implements SecretDao {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	@Override
	public void save(Secret secret) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(secret);
	}

	@Override
	public void saveOrUpdate(Secret secret) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().saveOrUpdate(secret);
	}

	@Override
	public Secret findSecretById(String secret_id) {
		// TODO Auto-generated method stub
		Secret secret = (Secret) sessionFactory.getCurrentSession().get(Secret.class, secret_id);
		return secret;
	}

	@Override
	public List<Secret> findSecretByUser(String client_id) {
		// TODO Auto-generated method stub
		List<Secret> secrets = (List<Secret>) sessionFactory.getCurrentSession().createQuery("from Secret as secret where secret.user.client_id = :client_id")
				.setParameter("client_id", client_id);
		return secrets;
	}

	@Override
	public void delete(Secret secret) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().delete(secret);
	}

}
