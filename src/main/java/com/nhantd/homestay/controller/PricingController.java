package com.nhantd.homestay.controller;

import com.nhantd.homestay.dto.request.PricingRequest;
import com.nhantd.homestay.dto.response.PricingResponse;
import com.nhantd.homestay.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pricing")
@RequiredArgsConstructor
public class PricingController {
    private final PricingService pricingService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<List<PricingResponse>> getAll() {
        return ResponseEntity.ok(pricingService.getAll());
    }

    @PreAuthorize("hasAnyRole(ADMIN')")
    @PostMapping
    public ResponseEntity<PricingResponse> create(@RequestBody PricingRequest request) {
        return ResponseEntity.ok(pricingService.create(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PricingResponse> update(@PathVariable Long id, @RequestBody PricingRequest request) {
        return ResponseEntity.ok(pricingService.update(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        pricingService.delete(id);
        return ResponseEntity.ok("Pricing deleted successfully");
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{branchId}/{roomTypeId}")
    public ResponseEntity<List<PricingResponse>> getByBranchAndType(
            @PathVariable Long branchId,
            @PathVariable Long roomTypeId) {
        return ResponseEntity.ok(pricingService.getByBranchAndType(branchId, roomTypeId));
    }
}
