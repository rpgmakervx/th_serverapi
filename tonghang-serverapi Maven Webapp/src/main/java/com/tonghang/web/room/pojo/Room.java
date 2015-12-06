package com.tonghang.web.room.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import com.tonghang.web.user.pojo.User;

@Component("room")
@Entity
@Table(name="rooms")
public class Room implements Serializable{

	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	private String room_id;
	
	@Column(name="meeting_id")
	private String meeting_id;
	
	@OneToOne()
	@JoinColumn(name="client_id")
	private User user;
	
	@Column(name="ry_id")
	private String ry_id;
	
	@Column(name="theme",columnDefinition="default null")
	private String theme;
	
	@Column(name="created_at")
	private Date created_at;
	
	@Column(name="open_at")
	private Date open_at;

	@Column(name="online")
	private int online;
	
	@ManyToMany()
	@JoinTable(name = "room_followers",
	   joinColumns = @JoinColumn(name = "room_id"),
	   inverseJoinColumns = @JoinColumn(name = "client_id"))
	private List<User> follower;
	
	@Column(name="member_num")
	private int member_num;
	public String getRoom_id() {
		return room_id;
	}

	public void setRoom_id(String room_id) {
		this.room_id = room_id;
	}
	
	public String getRy_id() {
		return ry_id;
	}

	public void setRy_id(String ry_id) {
		this.ry_id = ry_id;
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

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}
	
	public List<User> getFollower() {
		return follower;
	}

	public void setFollower(List<User> follower) {
		this.follower = follower;
	}
	
	public int getMember_num() {
		return member_num;
	}

	public void setMember_num(int member_num) {
		this.member_num = member_num;
	}

	@Override
	public String toString() {
		return "Room [room_id=" + room_id + ", meeting_id=" + meeting_id
				+ ", user=" + user + ", theme=" + theme + ", created_at="
				+ created_at + ", open_at=" + open_at + ", online=" + online
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((created_at == null) ? 0 : created_at.hashCode());
		result = prime * result
				+ ((follower == null) ? 0 : follower.hashCode());
		result = prime * result
				+ ((meeting_id == null) ? 0 : meeting_id.hashCode());
		result = prime * result + member_num;
		result = prime * result + online;
		result = prime * result + ((open_at == null) ? 0 : open_at.hashCode());
		result = prime * result + ((room_id == null) ? 0 : room_id.hashCode());
		result = prime * result + ((ry_id == null) ? 0 : ry_id.hashCode());
		result = prime * result + ((theme == null) ? 0 : theme.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		if (created_at == null) {
			if (other.created_at != null)
				return false;
		} else if (!created_at.equals(other.created_at))
			return false;
		if (follower == null) {
			if (other.follower != null)
				return false;
		} else if (!follower.equals(other.follower))
			return false;
		if (meeting_id == null) {
			if (other.meeting_id != null)
				return false;
		} else if (!meeting_id.equals(other.meeting_id))
			return false;
		if (member_num != other.member_num)
			return false;
		if (online != other.online)
			return false;
		if (open_at == null) {
			if (other.open_at != null)
				return false;
		} else if (!open_at.equals(other.open_at))
			return false;
		if (room_id == null) {
			if (other.room_id != null)
				return false;
		} else if (!room_id.equals(other.room_id))
			return false;
		if (ry_id == null) {
			if (other.ry_id != null)
				return false;
		} else if (!ry_id.equals(other.ry_id))
			return false;
		if (theme == null) {
			if (other.theme != null)
				return false;
		} else if (!theme.equals(other.theme))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
}
