package com.tonghang.web.app.dao;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.tonghang.web.app.pojo.Apk;

@Repository("apkDao")
public class ApkDaoImpl implements ApkDao {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	@Override
	public void save(Apk apk) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(apk);
	}

	@Override
	public void saveOrUpdate(Apk apk) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().saveOrUpdate(apk);
	}

	@Override
	public Apk findApkByCode(String app_code) {
		// TODO Auto-generated method stub
		return (Apk) sessionFactory.getCurrentSession().get(Apk.class, app_code);
	}

}
