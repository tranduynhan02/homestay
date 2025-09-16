package com.nhantd.homestay.service;

import com.nhantd.homestay.dto.request.PricingRequest;
import com.nhantd.homestay.dto.response.PricingResponse;
import com.nhantd.homestay.model.Branch;
import com.nhantd.homestay.model.Pricing;
import com.nhantd.homestay.model.RoomType;
import com.nhantd.homestay.repository.BranchRepository;
import com.nhantd.homestay.repository.PricingRepository;
import com.nhantd.homestay.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingService {
    private final PricingRepository pricingRepository;
    private final BranchRepository branchRepository;
    private final RoomTypeRepository roomTypeRepository;

    public List<PricingResponse> getAll() {
        return pricingRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PricingResponse create(PricingRequest request) {
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("Room type not found"));

        Pricing pricing = new Pricing();
        pricing.setBranch(branch);
        pricing.setRoomType(roomType);
        pricing.setComboName(request.getComboName());
        pricing.setPrice(request.getPrice());
        pricing.setMinDuration(request.getMinDuration());
        pricing.setMaxDuration(request.getMaxDuration());
        pricing.setStartTime(request.getStartTime());
        pricing.setEndTime(request.getEndTime());
        pricing.setWeekendOnly(request.getWeekendOnly());
        pricing.setHolidayOnly(request.getHolidayOnly());
        pricing.setExtraHourPrice(request.getExtraHourPrice());

        return toResponse(pricingRepository.save(pricing));
    }

    public List<PricingResponse> getByBranchAndType(Long branchId, Long roomTypeId) {
        return pricingRepository.findByBranchIdAndRoomTypeId(branchId, roomTypeId).stream()
                .map(this::toResponse)
                .toList();
    }

    public PricingResponse update(Long id, PricingRequest request) {
        Pricing pricing = pricingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pricing not found"));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("Room type not found"));

        pricing.setBranch(branch);
        pricing.setRoomType(roomType);
        pricing.setComboName(request.getComboName());
        pricing.setPrice(request.getPrice());
        pricing.setMinDuration(request.getMinDuration());
        pricing.setMaxDuration(request.getMaxDuration());
        pricing.setStartTime(request.getStartTime());
        pricing.setEndTime(request.getEndTime());
        pricing.setWeekendOnly(request.getWeekendOnly());
        pricing.setHolidayOnly(request.getHolidayOnly());
        pricing.setExtraHourPrice(request.getExtraHourPrice());

        return toResponse(pricingRepository.save(pricing));
    }

    public void delete(Long id) {
        if (!pricingRepository.existsById(id)) {
            throw new RuntimeException("Pricing not found");
        }
        pricingRepository.deleteById(id);
    }

    private PricingResponse toResponse(Pricing pricing) {
        PricingResponse dto = new PricingResponse();
        dto.setId(pricing.getId());
        dto.setBranchName(pricing.getBranch().getBranchName());
        dto.setRoomTypeName(pricing.getRoomType().getName());
        dto.setComboName(pricing.getComboName());
        dto.setPrice(pricing.getPrice());
        dto.setMinDuration(pricing.getMinDuration());
        dto.setMaxDuration(pricing.getMaxDuration());
        dto.setStartTime(pricing.getStartTime());
        dto.setEndTime(pricing.getEndTime());
        dto.setWeekendOnly(pricing.getWeekendOnly());
        dto.setHolidayOnly(pricing.getHolidayOnly());
        dto.setExtraHourPrice(pricing.getExtraHourPrice());
        return dto;
    }
}
