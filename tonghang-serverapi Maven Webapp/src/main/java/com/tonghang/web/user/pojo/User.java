package com.tonghang.web.user.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.stereotype.Component;

import com.tonghang.web.common.builder.Builder;
import com.tonghang.web.common.pojo.FeedBack;
import com.tonghang.web.common.util.SecurityUtil;
import com.tonghang.web.common.util.StringUtil;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.question.pojo.Question;
import com.tonghang.web.room.pojo.Room;
import com.tonghang.web.statistics.pojo.Channel;
import com.tonghang.web.topic.pojo.Topic;

/**
 * 
 * @author Administrator 用户信息包括：id 账号(number) 用户名(id) 密码(password) email
 *         电话号(phone) 性别(sex) 生日(birth) 状态(正常或封号) 是否在线(isonline) 所在省份(city),地市级(ext1,ext2)
 *         头像(image) 创建时间(created_time) 最近登录时间(last_login_time)关联属性(labellist:用户包含的标签，topic:用户加入的话题)
 * 			关联属性均由多的地方管理，所以user_topics关系由Topic类管理
 * 			salary(薪资) next_change(下次可以修改薪资信息的时间)
 * notice:城市信息格式为：province-city
 * 2015-12-11  新增建造者模式
 * 
 */
@Component("user")
@Entity
@Table(name="users")
public class User implements Serializable{
	
	@Id
	@GenericGenerator(strategy="assigned",name="idGenerator")
	@GeneratedValue(generator="idGenerator")
	private String client_id;

	@Column(name="username")
	private String username;
	//容联云要求的id必须是20位以内的数字字符串
	@Column(name="ry_id")
	private String ry_id;
	@Column(name="password")
	private String password;
	//时间戳
	@Column(name="image")
	private String image;
	@Column(name="email")
	private String email;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="sex")
	private String sex;
	
	@Column(name="birth")
	private String birth;
	
	@Column(name="status")
	private String status;
	
	@Column(name="isonline")
	private String isonline;
	
	@Column(name="province")
	private String province;
	
	@Column(name="city")
	private String city;
	
	@ManyToOne()
	@JoinColumn(name="channel_id")
	private Channel channel;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at",updatable=true)
	private Date created_at = new Date();
	@Column(name="")
	private Date next_change;
	
	@Column(name="last_login_at")
	private Date last_login_at;
	
	@ManyToMany()
	@JoinTable(name = "users_labels",
	   joinColumns = @JoinColumn(name = "client_id"),
	   inverseJoinColumns = @JoinColumn(name = "label_name"))
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Label> labellist;
	
	@ManyToOne()
	@JoinTable(name = "user_topics",
	   joinColumns = @JoinColumn(name = "client_id"),
	   inverseJoinColumns = @JoinColumn(name = "huanxin_group_id"))
	private Topic topic;
	
	@ManyToMany()
	@JoinTable(name="friends",
		joinColumns=@JoinColumn(name="client_id"),
		inverseJoinColumns=@JoinColumn(name="friend_id"))
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<User> friends;
	
	@OneToMany(mappedBy="user")
	private Set<FeedBack> feedbacks;
	
	@ManyToMany()
	@JoinTable(name="blacklist",
		joinColumns=@JoinColumn(name="client_id"),
		inverseJoinColumns=@JoinColumn(name="blocker_id"))
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<User> blacklist;
	
	@ManyToMany(mappedBy="follower")
	private Set<Room> follow;
	
	@OneToMany(mappedBy="asker")
	private Set<Question> questions;
	
	@OneToMany(mappedBy="anchor")
	private Set<Question> answerd;
	
	
	@Column(name="salary")
	private int salary;
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	public Set<Label> getLabellist() {
		return labellist;
	}
	public void setLabellist(Set<Label> labellist) {
		this.labellist = labellist;
	}
	
	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsonline() {
		return isonline;
	}

	public void setIsonline(String isonline) {
		this.isonline = isonline;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}

	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getLast_login_at() {
		return last_login_at;
	}

	public void setLast_login_at(Date last_login_at) {
		this.last_login_at = last_login_at;
	}
	public Set<User> getFriends() {
		return friends;
	}
	public void setFriends(Set<User> friends) {
		this.friends = friends;
	}
	public Set<FeedBack> getFeedbacks() {
		return feedbacks;
	}
	public void setFeedbacks(Set<FeedBack> feedbacks) {
		this.feedbacks = feedbacks;
	}
	
	public Set<User> getBlacklist() {
		return blacklist;
	}
	public void setBlacklist(Set<User> blacklist) {
		this.blacklist = blacklist;
	}
	
	public Set<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}
	public Set<Question> getAnswerd() {
		return answerd;
	}
	public void setAnswerd(Set<Question> answerd) {
		this.answerd = answerd;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	
	public Date getNext_change() {
		return next_change;
	}
	public void setNext_change(Date next_change) {
		this.next_change = next_change;
	}
	
	public String getRy_id() {
		//设置非空校验
		if(ry_id==null)
			this.setClient_id(SecurityUtil.getRYID(this.getClient_id()));
		return ry_id;
	}
	public void setRy_id(String ry_id) {
		this.ry_id = ry_id;
	}
	public Set<Room> getFollow() {
		return follow;
	}
	public void setFollow(Set<Room> follow) {
		this.follow = follow;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((client_id == null) ? 0 : client_id.hashCode());
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
		User other = (User) obj;
		if (client_id == null) {
			if (other.client_id != null)
				return false;
		} else if (!client_id.equals(other.client_id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "User [client_id=" + client_id + ", username=" + username
				+ ", labellist=" + labellist+ "]";
	}
//重构部分
	
	//获得所在城市信息（二级城市列表），城市信息不存在则设置为 未知
	public String getCityValue(){
		if(this.getProvince()==null||"".equals(this.getProvince()))
			return "未知";
		else return this.getCity()==null||"".equals(this.getCity())?this.getProvince():this.getProvince()+"-"+this.getCity();
	}
	//设置城市信息（二级城市列表）,不存在则两个变量均为null
	public void setCityValue(String city){
		if(city!=null){
			this.makeCityName(city);
		}else{
			this.city = null;
			this.province = null;
		}
	}
	//将前台的城市信息字符串转换为User对象的city和province
	private void makeCityName(String city){
		if(city.contains("-")){
			String pr = StringUtil.seperate(city, 0);
			String ci = StringUtil.seperate(city, 1);
			if(!ci.equals(this.getCity())&&city!=null)
				this.setCity(ci);
			if(!pr.equals(this.getProvince())&&pr!=null)
				this.setProvince(pr);
		}else{
			this.setProvince(city);
		}
	}
	//获取某用户的标签名
	public List<String> getLabelnames(){
		List<String> labels = new ArrayList<String>();
		if(this.getLabellist()!=null){
			for(Label l:this.getLabellist()){
				labels.add(l.getLabel_name());
			}
			return labels;			
		}else{
			return null;
		}
	}
	//当前用户是否被封号
	public boolean isIsolate(){
		return this.getStatus().equals("0");
	}
	//建造者模式，建造者在构建的时候检查  要构建的字段是不是和以前的对象一样
	//如果一样则不覆盖以前的字段，否则覆盖
	//构建者不能构建null的属性
	public class UserBuilder implements Builder<User>{
		public UserBuilder setClient_id(String client_id){
			User.this.setClient_id(client_id);
			return this;
		}
		public UserBuilder setUsername(String username) {
			if(username!=null&&!username.equals(User.this.getUsername()))
				User.this.setUsername(username);
			return this;
		}

		public UserBuilder setRy_id(String ry_id) {
			if(ry_id!=null&&!ry_id.equals(User.this.getRy_id()))
				User.this.setRy_id(ry_id);
			return this;
		}

		public UserBuilder setPassword(String password) {
			if(password!=null&&!password.equals(User.this.getPassword()))
				User.this.setPassword(password);
			return this;
		}

		public UserBuilder setImage(String image) {
			if(image!=null&&!image.equals(User.this.getImage()))
				User.this.setImage(image);
			return this;
		}

		public UserBuilder setEmail(String email) {
			if(email!=null&&!email.equals(User.this.getEmail()))
				User.this.setEmail(email);
			return this;
		}

		public UserBuilder setPhone(String phone) {
			if(phone!=null&&!phone.equals(User.this.getPhone()))
				User.this.setPhone(phone);
			return this;
		}

		public UserBuilder setSex(String sex) {
			if(sex!=null&&!sex.equals(User.this.getSex()))
				User.this.setSex(sex);
			return this;
		}

		public UserBuilder setBirth(String birth) {
			if(birth!=null&&!birth.equals(User.this.getBirth()))
				User.this.setBirth(birth);
			return this;
		}

		public UserBuilder setStatus(String status) {
			if(status!=null&&!status.equals(User.this.getStatus()))
				User.this.setStatus(status);
			return this;
		}

		public UserBuilder setIsonline(String isonline) {
			if(isonline!=null&&!isonline.equals(User.this.getIsonline()))
				User.this.setIsonline(isonline);
			return this;
		}
		//直接建造city的完整格式
		public UserBuilder setCity(String city) {
			if(city!=null&&!city.equals(User.this.getCityValue()))
				User.this.setCityValue(city);
			return this;
		}

		public UserBuilder setCreated_at(Date created_at) {
			User.this.setCreated_at(created_at);
			return this;
		}

		public UserBuilder setNext_change(Date next_change) {
			User.this.setNext_change(next_change);
			return this;
		}

		public UserBuilder setLast_login_at(Date last_login_at) {
			User.this.setLast_login_at(last_login_at);
			return this;
		}

		@Override
		public User build() {
			// TODO Auto-generated method stub
			return User.this;
		}
		@Override
		public UserBuilder create() {
			// TODO Auto-generated method stub
			return this;
		}
	}
}
