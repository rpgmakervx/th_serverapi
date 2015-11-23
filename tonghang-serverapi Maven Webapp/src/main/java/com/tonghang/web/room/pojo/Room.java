package com.tonghang.web.room.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.stereotype.Component;

import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.user.pojo.User;

@Component("room")
@Entity
@Table(name="rooms")
public class Room implements Serializable{

	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	public String room_id;
	
	@Column(name="meeting_id")
	public String meeting_id;
	
	@OneToOne()
	@JoinColumn(name="client_id")
	public User user;
	
	@Column(name="theme",columnDefinition="default null")
	public String theme;
	
	@Column(name="created_at")
	public Date created_at;
	
	@Column(name="open_at")
	public Date open_at;

	public String getRoom_id() {
		return room_id;
	}

	public void setRoom_id(String room_id) {
		this.room_id = room_id;
	}
	
	public String getMeeting_id() {
		return meeting_id;
	}

	public void setMeeting_id(String meeting_id) {
		this.meeting_id = meeting_id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getOpen_at() {
		return open_at;
	}

	public void setOpen_at(Date open_at) {
		this.open_at = open_at;
	}

	@Override
	public String toString() {
		return "Room [room_id=" + room_id + ", meeting_id=" + meeting_id
				+ ", user=" + user + ", theme=" + theme + ","
				+ ", created_at=" + created_at + "]";
	}
	
}
