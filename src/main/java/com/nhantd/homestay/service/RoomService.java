package com.nhantd.homestay.service;

import com.nhantd.homestay.dto.request.BranchRequest;
import com.nhantd.homestay.dto.request.RoomRequest;
import com.nhantd.homestay.dto.request.RoomTypeRequest;
import com.nhantd.homestay.dto.response.BranchResponse;
import com.nhantd.homestay.dto.response.RoomResponse;
import com.nhantd.homestay.dto.response.RoomTypeResponse;
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

    // ==========================
    // BRANCH
    // ==========================

    public BranchResponse createBranch(BranchRequest request) {
        Branch branch = new Branch();
        branch.setBranchName(request.getName());
        branch.setAddress(request.getAddress());
        branch.setPhone(request.getPhone());
        return toResponse(branchRepository.save(branch));
    }

    public List<BranchResponse> getAllBranches() {
        return branchRepository.findAll().stream().map(this::toResponse).toList();
    }

    public BranchResponse getBranchById(Long id) {
        return branchRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
    }

    public void deleteBranch(Long id) {
        branchRepository.deleteById(id);
    }

    public BranchResponse updateBranch(Long id, BranchRequest request) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        branch.setBranchName(request.getName());
        branch.setAddress(request.getAddress());
        branch.setPhone(request.getPhone());
        return toResponse(branchRepository.save(branch));
    }

    private BranchResponse toResponse(Branch branch) {
        BranchResponse dto = new BranchResponse();
        dto.setId(branch.getId());
        dto.setName(branch.getBranchName());
        dto.setAddress(branch.getAddress());
        dto.setPhone(branch.getPhone());
        return dto;
    }

    // ==========================
    // ROOM TYPE
    // ==========================

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

    // ==========================
    // ROOM
    // ==========================

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
