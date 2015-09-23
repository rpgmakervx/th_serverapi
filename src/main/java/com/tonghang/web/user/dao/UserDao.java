package com.tonghang.web.user.dao;

import java.util.List;

import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.user.pojo.User;

public interface UserDao {
	
	public void save(User user);
	public void addFriend(User my,User friend);
	public void addBlocker(User me,User blocker);
	
	public User findUserByEmail(String email);
	public User findUserByNickName(String nickname);
	
	public User findUserById(String client_id);
	public List<User> findAllUser();
	public List<User> findUserByUsernameUnique(String username);
	public List<User> findUserByUsername(String username,int page);
	public List<User> findUserByLabel(String label_name,int nowpage);
	//按注册时间 从最新的用户中抽取1个用户，用户指定区间
	public List<User> findOneUserByCreatedAtDesc(int begin,int end);
	
	public void update(String client_id,User user);
	public void saveOrUpdate(User user);
	public void deleteLabel(String client_id,Label label);
	public void deleteAllLabel(String client_id);
	public void deleteFriend(User my,User friend);
	
	public void deleteBlock(User me,User blcoker);
	
}
