package com.tonghang.web.validate.dao;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.tonghang.web.user.pojo.User;
import com.tonghang.web.validate.pojo.ValidateCode;

@Repository("validateDao")
public class ValidateDaoImpl implements ValidateDao {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	@Override
	public ValidateCode findCodeByUser(String client_id) {
		// TODO Auto-generated method stub
		ValidateCode validcode = (ValidateCode)sessionFactory.getCurrentSession().get(ValidateCode.class, client_id);
		return validcode;
	}

	@Override
	public void save(ValidateCode code) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(code);
	}

	@Override
	public void saveOrUpdate(ValidateCode code) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().saveOrUpdate(code);
	}

}
