package com.tonghang.web.record.dao;

import com.tonghang.web.record.pojo.Record;

public interface RecordDao {

	public void save(Record record);
	
	public void saveOrUpdate(Record record);
}
