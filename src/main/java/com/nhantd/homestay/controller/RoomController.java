package com.nhantd.homestay.controller;

import com.nhantd.homestay.dto.request.BranchRequest;
import com.nhantd.homestay.dto.request.RoomRequest;
import com.nhantd.homestay.dto.request.RoomTypeRequest;
import com.nhantd.homestay.dto.response.BranchResponse;
import com.nhantd.homestay.dto.response.RoomResponse;
import com.nhantd.homestay.dto.response.RoomTypeResponse;
import com.nhantd.homestay.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    // ==========================
    // BRANCH
    // ==========================

    @GetMapping("/branches")
    public ResponseEntity<List<BranchResponse>> getAllBranches() {
        return ResponseEntity.ok(roomService.getAllBranches());
    }

    @PostMapping("/branches")
    public ResponseEntity<BranchResponse> createBranch(@RequestBody BranchRequest request) {
        return ResponseEntity.ok(roomService.createBranch(request));
    }

    @PutMapping("/branches/{id}")
    public ResponseEntity<BranchResponse> updateBranch(@PathVariable Long id, @RequestBody BranchRequest request) {
        return ResponseEntity.ok(roomService.updateBranch(id, request));
    }

    @DeleteMapping("/branches/{id}")
    public ResponseEntity<String> deleteBranch(@PathVariable Long id) {
        roomService.deleteBranch(id);
        return ResponseEntity.ok("Branch deleted successfully");
    }

    // ==========================
    // ROOM TYPE
    // ==========================

    @GetMapping("/room-types")
    public ResponseEntity<List<RoomTypeResponse>> getAll() {
        return ResponseEntity.ok(roomService.getAll());
    }

    @PostMapping("/room-types")
    public ResponseEntity<RoomTypeResponse> createRoomType(@RequestBody RoomTypeRequest request) {
        return ResponseEntity.ok(roomService.createRoomType(request));
    }

    @PutMapping("/room-types/{id}")
    public ResponseEntity<RoomTypeResponse> updateRoomType(@PathVariable Long id, @RequestBody RoomTypeRequest request) {
        return ResponseEntity.ok(roomService.updateRoomType(id, request));
    }

    @DeleteMapping("/room-types/{id}")
    public ResponseEntity<String> deleteRoomType(@PathVariable Long id) {
        roomService.deleteRoomType(id);
        return ResponseEntity.ok("Room Type deleted");
    }

    // ==========================
    // ROOM
    // ==========================

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.createRoom(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long id, @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }
}
