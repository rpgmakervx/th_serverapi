package com.tonghang.web.card.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.GenericGenerator;

import com.tonghang.web.user.pojo.User;
/**
 * 该实体保存用户修改名片的记录，attribute字段记录
 * @author Administrator
 *
 */
@Table(name="cardhistory")
@Entity
public class CardHistory implements Serializable{

	@ManyToOne()
	@JoinColumn(name="client_id",columnDefinition="default null")
	@ForeignKey(name="null")
	private User user;
	
	@Column(name="attribute")
	private String attribute;
	@Column(name="value")
	private String value;
	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	@Column(name="change_at")
	private Date change_at;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Date getChange_at() {
		return change_at;
	}
	public void setChange_at(Date change_at) {
		this.change_at = change_at;
	}
	
}
