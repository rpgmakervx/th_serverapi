package com.tonghang.web.user.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

@Component("validateCode")
@Entity
@Table(name="validatecode")
public class ValidateCode {

	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	@OneToOne()
	@JoinColumn(name="client_id",columnDefinition="default null")
	private User user;
	
	@Column(name="phone_code")
	private String phone_code;
	
	@Column(name="phone_timestamp")
	private String phone_timestamp;
	
	@Column(name="email_code")
	private String email_code;
	
	@Column(name="email_timestamp")
	private String email_timestamp;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPhone_code() {
		return phone_code;
	}

	public void setPhone_code(String phone_code) {
		this.phone_code = phone_code;
	}

	public String getPhone_timestamp() {
		return phone_timestamp;
	}

	public void setPhone_timestamp(String phone_timestamp) {
		this.phone_timestamp = phone_timestamp;
	}

	public String getEmail_code() {
		return email_code;
	}

	public void setEmail_code(String email_code) {
		this.email_code = email_code;
	}

	public String getEmail_timestamp() {
		return email_timestamp;
	}

	public void setEmail_timestamp(String email_timestamp) {
		this.email_timestamp = email_timestamp;
	}
	
}
