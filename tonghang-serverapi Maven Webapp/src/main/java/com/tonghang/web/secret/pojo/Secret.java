package com.tonghang.web.secret.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import com.tonghang.web.user.pojo.User;

@Component("secret")
@Entity
@Table(name="secret")
public class Secret implements Serializable{

	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	public String secret_id;
	
	@ManyToOne()
	@JoinColumn(name="client_id")
	private User user;
	
	@Column(name="title")
	private String title;
	
	@Column(name="content")
	private String content;
	
	@Column(name="created_at")
	private Date created_at;

	public String getSecret_id() {
		return secret_id;
	}

	public void setSecret_id(String secret_id) {
		this.secret_id = secret_id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	
	
}
