package com.nhantd.homestay.controller;

import com.nhantd.homestay.dto.request.PricingRequest;
import com.nhantd.homestay.dto.response.PricingResponse;
import com.nhantd.homestay.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pricing")
@RequiredArgsConstructor
public class PricingController {
    private final PricingService pricingService;

    @GetMapping
    public ResponseEntity<List<PricingResponse>> getAll() {
        return ResponseEntity.ok(pricingService.getAll());
    }

    @PostMapping
    public ResponseEntity<PricingResponse> create(@RequestBody PricingRequest request) {
        return ResponseEntity.ok(pricingService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PricingResponse> update(@PathVariable Long id, @RequestBody PricingRequest request) {
        return ResponseEntity.ok(pricingService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        pricingService.delete(id);
        return ResponseEntity.ok("Pricing deleted successfully");
    }

    @GetMapping("/{branchId}/{roomTypeId}")
    public ResponseEntity<List<PricingResponse>> getByBranchAndType(
            @PathVariable Long branchId,
            @PathVariable Long roomTypeId) {
        return ResponseEntity.ok(pricingService.getByBranchAndType(branchId, roomTypeId));
    }
}
