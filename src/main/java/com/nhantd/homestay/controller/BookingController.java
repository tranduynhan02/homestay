package com.nhantd.homestay.controller;

import com.nhantd.homestay.dto.request.BookingRequest;
import com.nhantd.homestay.dto.response.BookingResponse;
import com.nhantd.homestay.enums.BookingStatus;
import com.nhantd.homestay.model.CustomUserDetails;
import com.nhantd.homestay.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/me")
    public ResponseEntity<List<BookingResponse>> getMyBookings(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long customerId = userDetails.getUser().getId();
        return ResponseEntity.ok(bookingService.getBookingsByCustomer(customerId));
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BookingRequest request) {
        Long customerId = null;
        if (userDetails != null) {
            customerId = userDetails.getUser().getId();
        }
        return ResponseEntity.ok(bookingService.createBooking(customerId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BookingRequest request) {
        Long customerId = null;
        if (userDetails != null) {
            customerId = userDetails.getUser().getId();
        }
        return ResponseEntity.ok(bookingService.updateBooking(id, request, customerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BookingResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status) {
        return ResponseEntity.ok(bookingService.updateStatus(id, status));
    }
}
