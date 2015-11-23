package com.tonghang.web.record.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.record.dao.RecordDao;
import com.tonghang.web.record.pojo.Record;

@Service("recordService")
@Transactional
public class RecordService {

	@Resource(name="recordDao")
	private RecordDao recordDao;
	
	/**
	 * 保存进出房间记录
	 * @param record
	 */
	public void recordAction(Record record){
		recordDao.save(record);
	}
}
