package com.nhantd.homestay.repository;

import com.nhantd.homestay.model.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PricingRepository extends JpaRepository<Pricing, Long> {
    List<Pricing> findByBranchIdAndRoomTypeId(Long branchId, Long roomTypeId);
}
