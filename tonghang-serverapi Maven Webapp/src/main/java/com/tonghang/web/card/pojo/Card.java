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
 * 每个用户只有一个名片
 * @author Administrator
 *
 */
@Table(name="card")
@Entity
public class Card implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	@ManyToOne()
	@JoinColumn(name="client_id",columnDefinition="default null")
	private User user;
	
	@Column(name="companyname")
	private String companyname;
	@Column(name="realname")
	private String realname;
	@Column(name="position")
	private String position;
	@Column(name="work_date")
	private String work_date;
	@Column(name="email")
	private String email;
	@Column(name="phone")
	private String phone;
	@Column(name="schoolname")
	private String schoolname;
	@Column(name="major")
	private String major;
	@Column(name="diploma")
	private String diploma;
	@Column(name="school_date")
	private String school_date;
	@Column(name="exchange_times")
	private int exchange_times;
	@Column(name="chat_times")
	private int chat_times;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getWork_date() {
		return work_date;
	}
	public void setWork_date(String work_date) {
		this.work_date = work_date;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSchoolname() {
		return schoolname;
	}
	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getDiploma() {
		return diploma;
	}
	public void setDiploma(String diploma) {
		this.diploma = diploma;
	}
	public String getSchool_date() {
		return school_date;
	}
	public void setSchool_date(String school_date) {
		this.school_date = school_date;
	}
	public int getExchange_times() {
		return exchange_times;
	}
	public void setExchange_times(int exchange_times) {
		this.exchange_times = exchange_times;
	}
	
	public int getChat_times() {
		return chat_times;
	}
	public void setChat_times(int chat_times) {
		this.chat_times = chat_times;
	}
	@Override
	public String toString() {
		return "Card [user=" + user + ", companyname=" + companyname
				+ ", position=" + position + ", work_date=" + work_date
				+ ", email=" + email + ", phone=" + phone + ", schoolname="
				+ schoolname + ", major=" + major + ", diploma=" + diploma
				+ ", school_date=" + school_date + ", exchange_times="
				+ exchange_times + "]";
	}
	
	
}
