package com.tonghang.web.validate.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tonghang.web.validate.dao.ValidateDao;
import com.tonghang.web.validate.pojo.ValidateCode;

@Service("validateService")
public class ValidateCodeService {

	@Resource(name="validateDao")
	private ValidateDao validateDao;
	
	/**
	 * 获得验证码实体
	 * @param client_id
	 * @return
	 */
	public ValidateCode findValidateCode(String client_id){
		return validateDao.findCodeByUser(client_id);
	}
	/**
	 * 修改验证码时效或验证码
	 * @param code
	 */
	public void updateValidateCode(ValidateCode code){
		validateDao.saveOrUpdate(code);
	}
	/**
	 * 新建验证码
	 * @param code
	 */
	public void addValidateCode(ValidateCode code){
		validateDao.save(code);
	}
}
