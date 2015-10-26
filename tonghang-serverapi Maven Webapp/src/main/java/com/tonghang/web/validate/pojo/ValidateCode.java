package com.tonghang.web.validate.pojo;

import java.io.Serializable;

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

import com.tonghang.web.user.pojo.User;

/**
 * 该实体分别保存 手机和邮箱的 验证码 以及验证码创建时间
 * @author Administrator
 *
 */
@Component("validateCode")
@Entity
@Table(name="validatecode")
public class ValidateCode implements Serializable{

	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	@OneToOne()
	@JoinColumn(name="client_id",columnDefinition="default null")
	private User user;
	
	@Column(name="phone_code")
	private String phone_code;
	
	@Column(name="phone_timestamp")
	private Long phone_timestamp;
	
	@Column(name="email_code")
	private String email_code;
	
	@Column(name="email_timestamp")
	private Long email_timestamp;

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

	public Long getPhone_timestamp() {
		return phone_timestamp;
	}

	public void setPhone_timestamp(Long phone_timestamp) {
		this.phone_timestamp = phone_timestamp;
	}

	public String getEmail_code() {
		return email_code;
	}

	public void setEmail_code(String email_code) {
		this.email_code = email_code;
	}

	public Long getEmail_timestamp() {
		return email_timestamp;
	}

	public void setEmail_timestamp(Long email_timestamp) {
		this.email_timestamp = email_timestamp;
	}
	
}
