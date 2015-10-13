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
	
	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	@Column(name="change_at")
	private String change_at;
	
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
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getChange_at() {
		return change_at;
	}
	public void setChange_at(String change_at) {
		this.change_at = change_at;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
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
	
	
}
