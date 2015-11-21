package com.tonghang.web.question.pojo;

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

@Component("question")
@Entity
@Table(name="questions")
public class Question {

	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	public String question_id;
	
	@ManyToOne()
	@JoinColumn(name="anchor_id")
	public User anchor;
	
	@ManyToOne()
	@JoinColumn(name="asker_id")
	public User asker;
	
	@Column(name="content")
	private String content;
	
	@Column(name="created_at")
	private Date created_at;

	public String getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}

	public User getAnchor() {
		return anchor;
	}

	public void setAnchor(User anchor) {
		this.anchor = anchor;
	}

	public User getAsker() {
		return asker;
	}

	public void setAsker(User asker) {
		this.asker = asker;
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
