package com.nhantd.homestay.service.impl;

import com.nhantd.homestay.model.Room;
import com.nhantd.homestay.repository.RoomRepository;
import com.nhantd.homestay.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    @Override
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Long id, Room room) {
        Room existing = roomRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setCode(room.getCode());
            existing.setType(room.getType());
            existing.setDescription(room.getDescription());
            existing.setPrice(room.getPrice());
            existing.setAvailable(room.getAvailable());
            return roomRepository.save(existing);
        }
        return null;
    }

    @Override
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    @Override
    public Room getRoomByCode(String code) {
        return roomRepository.findByCode(code).orElse(null);
    }
}
