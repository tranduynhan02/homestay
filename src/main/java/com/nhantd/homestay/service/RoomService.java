package com.nhantd.homestay.service;

import com.nhantd.homestay.model.Room;

import java.util.List;

public interface RoomService {
    List<Room> getAllRooms();
    Room getRoomById(Long id);
    Room createRoom(Room room);
    Room updateRoom(Long id, Room room);
    void deleteRoom(Long id);
    Room getRoomByCode(String code);
}
