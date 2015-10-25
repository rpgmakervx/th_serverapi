package com.tonghang.web.validate.dao;

import com.tonghang.web.validate.pojo.ValidateCode;

public interface ValidateDao {

	public ValidateCode findCodeByUser(String client_id);
	
	public void save(ValidateCode code);
	
	public void saveOrUpdate(ValidateCode code);
}
