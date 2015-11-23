package com.tonghang.web.record.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;

import com.tonghang.web.record.dao.RecordDao;
import com.tonghang.web.record.pojo.Record;

public class RecordDaoImpl implements RecordDao {

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	@Override
	public void save(Record record) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(record);
	}

	@Override
	public void saveOrUpdate(Record record) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().saveOrUpdate(record);
	}

}
