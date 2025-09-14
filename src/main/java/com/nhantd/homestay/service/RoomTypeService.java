package com.nhantd.homestay.service;

import com.nhantd.homestay.dto.request.RoomTypeRequest;
import com.nhantd.homestay.dto.response.RoomTypeResponse;
import com.nhantd.homestay.model.RoomType;
import com.nhantd.homestay.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeResponse createRoomType(RoomTypeRequest request) {
        RoomType type = new RoomType();
        type.setName(request.getName());
        return toResponse(roomTypeRepository.save(type));
    }

    public List<RoomTypeResponse> getAll() {
        return roomTypeRepository.findAll().stream().map(this::toResponse).toList();
    }

    public RoomTypeResponse updateRoomType(Long id, RoomTypeRequest request) {
        RoomType type = roomTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("RoomType not found"));
        type.setName(request.getName());
        return toResponse(roomTypeRepository.save(type));
    }

    public void deleteRoomType(Long id) {
        roomTypeRepository.deleteById(id);
    }

    private RoomTypeResponse toResponse(RoomType type) {
        RoomTypeResponse dto = new RoomTypeResponse();
        dto.setId(type.getId());
        dto.setName(type.getName());
        return dto;
    }
}
