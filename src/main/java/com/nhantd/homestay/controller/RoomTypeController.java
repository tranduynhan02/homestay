package com.nhantd.homestay.controller;

import com.nhantd.homestay.dto.request.RoomTypeRequest;
import com.nhantd.homestay.dto.response.RoomTypeResponse;
import com.nhantd.homestay.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room-types")
@RequiredArgsConstructor
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @GetMapping
    public ResponseEntity<List<RoomTypeResponse>> getAll() {
        return ResponseEntity.ok(roomTypeService.getAll());
    }

    @PostMapping
    public ResponseEntity<RoomTypeResponse> createRoomType(@RequestBody RoomTypeRequest request) {
        return ResponseEntity.ok(roomTypeService.createRoomType(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeResponse> updateRoomType(@PathVariable Long id, @RequestBody RoomTypeRequest request) {
        return ResponseEntity.ok(roomTypeService.updateRoomType(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoomType(@PathVariable Long id) {
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.ok("Room Type deleted");
    }
}
