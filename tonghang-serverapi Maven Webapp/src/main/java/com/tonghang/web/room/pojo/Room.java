package com.tonghang.web.room.pojo;

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
import org.springframework.stereotype.Component;

import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.user.pojo.User;

@Component("room")
@Entity
@Table(name="rooms")
public class Room {

	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	public String room_id;
	
	@Column(name="meeting_id")
	public String meeting_id;
	
	@OneToOne()
	@JoinColumn(name="client_id",columnDefinition="default null")
	public User user;
	
	@Column(name="theme")
	public String theme;
	
	@ManyToMany()
	@JoinTable(name = "rooms_labels",
	   joinColumns = @JoinColumn(name = "label_name"),
	   inverseJoinColumns = @JoinColumn(name = "room_id"))
	public Set<Label> labels;
	
	@Column(name="created_at")
	public Date created_at;

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


	public Set<Label> getLabels() {
		return labels;
	}

	public void setLabels(Set<Label> labels) {
		this.labels = labels;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	
}
