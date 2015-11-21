package com.tonghang.web.room.dao.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.tonghang.web.room.dao.RoomDao;
import com.tonghang.web.room.pojo.Room;
import com.tonghang.web.user.pojo.User;

@Repository("roomDao")
@Transactional
public class RoomDaoImpl implements RoomDao{
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;

	@Override
	public void save(Room room) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(room);
	}

	@Override
	public void saveOrUpdate(Room room) {
		// TODO Auto-generated method stub 
		sessionFactory.getCurrentSession().saveOrUpdate(room); 
	}

	@Override
	public Room findRoomById(String room_id) {
		// TODO Auto-generated method stub
		Room room = (Room) sessionFactory.getCurrentSession().get(Room.class, room_id);
		return room;
	}

	@Override
	public Room findRoomByOwner(String client_id) {
		// TODO Auto-generated method stub
		Room room = (Room) sessionFactory.getCurrentSession().createQuery("from Room as room where room.user.client_id = :client_id")
							.setParameter("client_id", client_id).uniqueResult();
		return room;
	}

	@Override
	public List<Room> findAllRoom() {
		// TODO Auto-generated method stub
		List<Room> room = (List<Room>) sessionFactory.getCurrentSession().createQuery("from Room as room").list();
		return room;
	}

	@Override
	public List<Room> findRoomByLabel(String label_name) {
		// TODO Auto-generated method stub
		Query query = sessionFactory.getCurrentSession().createQuery("select distinct room from Room as room left join room.labels as " +
				"label where lower(label.label_name) like concat('%',lower(:label_name),'%') order by room.created_at");
		List<User> user = (List<User>) query.setParameter("label_name", label_name).list();
		return null;
	}

}
