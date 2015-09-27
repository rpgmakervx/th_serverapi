package com.tonghang.web.app.dao;

import com.tonghang.web.app.pojo.Apk;

public interface ApkDao {

	public void save(Apk apk);
	
	public void saveOrUpdate(Apk apk);
	
	public Apk findApkByCode(String app_code);
}
