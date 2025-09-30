package com.nhantd.homestay.controller;

import com.nhantd.homestay.dto.request.BookingRequest;
import com.nhantd.homestay.dto.response.BookingResponse;
import com.nhantd.homestay.dto.response.FreeSlotResponse;
import com.nhantd.homestay.enums.BookingStatus;
import com.nhantd.homestay.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        if (userDetails != null && request.getCustomerId() == null) {
            request.setCustomerId(userDetails.getUser().getId());
        }
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(
            @PathVariable Long id,
            @RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.updateBooking(id, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BookingResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status) {
        return ResponseEntity.ok(bookingService.updateStatus(id, status));
    }

    @GetMapping("/availability/weekly")
    public ResponseEntity<Map<LocalDate, List<FreeSlotResponse>>> getWeeklyAvailability(
            @RequestParam Long branchId) {
        return ResponseEntity.ok(bookingService.getWeeklyAvailability(branchId));
    }
}
