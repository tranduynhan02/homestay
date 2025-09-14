package com.nhantd.homestay.service;

import com.nhantd.homestay.dto.request.RoomRequest;
import com.nhantd.homestay.dto.response.RoomResponse;
import com.nhantd.homestay.model.Branch;
import com.nhantd.homestay.model.Room;
import com.nhantd.homestay.model.RoomType;
import com.nhantd.homestay.repository.BranchRepository;
import com.nhantd.homestay.repository.RoomRepository;
import com.nhantd.homestay.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final BranchRepository branchRepository;
    private final RoomTypeRepository roomTypeRepository;

    public RoomResponse createRoom(RoomRequest request) {
        Branch branch = branchRepository.findById(request.getBranch_id())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        RoomType roomType = roomTypeRepository.findById(request.getType_id())
                .orElseThrow(() -> new RuntimeException("RoomType not found"));

        Room room = new Room();
        room.setRoom_name(request.getRoom_name());
        room.setDescription(request.getDescription());
        room.setAvailable(true);
        room.setBranch(branch);
        room.setType(roomType);
        room.setHasBathtub(request.getHasBathtub());
        room.setHasBalcony(request.getHasBalcony());
        room.setHasKitchen(request.getHasKitchen());

        return toResponse(roomRepository.save(room));
    }

    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<RoomResponse> getRoomsByBranch(Long branchId) {
        return roomRepository.findByBranchId(branchId).stream().map(this::toResponse).toList();
    }

    public RoomResponse updateRoom(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Branch branch = branchRepository.findById(request.getBranch_id())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        RoomType roomType = roomTypeRepository.findById(request.getType_id())
                .orElseThrow(() -> new RuntimeException("RoomType not found"));

        room.setRoom_name(request.getRoom_name());
        room.setDescription(request.getDescription());
        room.setBranch(branch);
        room.setType(roomType);
        room.setHasBathtub(request.getHasBathtub());
        room.setHasBalcony(request.getHasBalcony());
        room.setHasKitchen(request.getHasKitchen());

        return toResponse(roomRepository.save(room));
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    private RoomResponse toResponse(Room room) {
        RoomResponse dto = new RoomResponse();
        dto.setId(room.getId());
        dto.setRoom_name(room.getRoom_name());
        dto.setDescription(room.getDescription());
        dto.setAvailable(room.getAvailable());
        dto.setBranch_name(room.getBranch().getBranchName());
        dto.setType_name(room.getType().getName());
        dto.setHasBathtub(room.getHasBathtub());
        dto.setHasBalcony(room.getHasBalcony());
        dto.setHasKitchen(room.getHasKitchen());
        return dto;
    }
}
