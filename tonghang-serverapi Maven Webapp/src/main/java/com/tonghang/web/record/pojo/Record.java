package com.tonghang.web.record.pojo;
/**
 * 记录用户进出房间的时间和房间号
 */
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

import com.tonghang.web.room.pojo.Room;
import com.tonghang.web.user.pojo.User;

@Entity
@Table(name="record")
public class Record implements Serializable {
	
	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	@ManyToOne()
	@JoinColumn(name="client_id",columnDefinition="default null")
	private User user;

	@ManyToOne()
	@JoinColumn(name="room_id",columnDefinition="default null")
	private Room room;
	
	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	@Column(name="join_at")
	private Date join_at;

	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	@Column(name="leave_at")
	private Date leave_at;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Date getJoin_at() {
		return join_at;
	}

	public void setJoin_at(Date join_at) {
		this.join_at = join_at;
	}

	public Date getLeave_at() {
		return leave_at;
	}

	public void setLeave_at(Date leave_at) {
		this.leave_at = leave_at;
	}
	
}
