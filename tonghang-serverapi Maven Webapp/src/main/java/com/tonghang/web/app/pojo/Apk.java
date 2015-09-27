package com.tonghang.web.app.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Table(name="apk")
@Entity
public class Apk {

	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	public String app_code;
	@Column(name="app_version")
	public String app_version;
	@Column(name="upload_at")
	public Date upload_at;
	public String getApp_code() {
		return app_code;
	}
	public void setApp_code(String app_code) {
		this.app_code = app_code;
	}
	public String getApp_version() {
		return app_version;
	}
	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}
	public Date getUpload_at() {
		return upload_at;
	}
	public void setUpload_at(Date upload_at) {
		this.upload_at = upload_at;
	}
	
}
