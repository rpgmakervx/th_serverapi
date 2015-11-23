package com.tonghang.web.room.dao;

import java.util.List;

import com.tonghang.web.room.pojo.Room;

public interface RoomDao {

	public void save(Room room);
	
	public void saveOrUpdate(Room room);
	
	public Room findRoomById(String room_id);
	
	public Room findRoomByOwner(String client_id);
	
	public List<Room> findAllRoom();
	
	public List<Room> findRoomByLabel(String label_name);
	
	public List<Room> findRoomSortByOpenat();
	public List<Room> findRoomSortByCreatedat();
}
