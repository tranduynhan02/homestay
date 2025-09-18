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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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

    public double calculatePrice(Branch branch, RoomType roomType, LocalDateTime checkIn, LocalDateTime checkOut) {
        List<Pricing> pricings = pricingRepository.findByBranchIdAndRoomTypeId(
                branch.getId(), roomType.getId()
        );
        if (pricings.isEmpty()) {
            throw new RuntimeException("No pricing configured for this branch & room type");
        }

        Pricing dayUse = findByName(pricings, "Day Use");
        Pricing overnight = findByName(pricings, "Overnight");
        Pricing combo5h = findByName(pricings, "Combo 5h");
        Pricing combo3h = findByName(pricings, "Combo 3h");

        long totalMinutes = ChronoUnit.MINUTES.between(checkIn, checkOut);
        if (totalMinutes <= 0) throw new RuntimeException("Invalid booking time");

        double total = 0.0;
        LocalDateTime cursor = checkIn;

        if (dayUse != null && dayUse.getMaxDuration() != null) {
            while (cursor.isBefore(checkOut)) {
                LocalTime t = cursor.toLocalTime();

                if (!t.isBefore(LocalTime.NOON) && t.isBefore(LocalTime.of(14, 0))) {
                    LocalDateTime freeUntil = LocalDateTime.of(cursor.toLocalDate(), LocalTime.of(14, 0));
                    cursor = min(freeUntil, checkOut);
                    if (!cursor.isBefore(checkOut)) break;
                }

                if (!t.isBefore(LocalTime.of(14, 0))) {
                    LocalDateTime blockEnd = LocalDateTime.of(cursor.toLocalDate().plusDays(1), LocalTime.NOON);
                    LocalDateTime actualEnd = min(blockEnd, checkOut);
                    long blockHours = ceilHoursBetween(cursor, actualEnd);

                    if (dayUse.getMinDuration() == null || blockHours >= dayUse.getMinDuration()) {
                        total += dayUse.getPrice();
                        cursor = actualEnd;

                        if (blockEnd.equals(actualEnd) && cursor.isBefore(checkOut)) {
                            LocalDateTime freeUntil = LocalDateTime.of(cursor.toLocalDate(), LocalTime.of(14, 0));
                            cursor = min(freeUntil, checkOut);
                        }
                        continue;
                    }
                }
                break;
            }
        }

        if (!cursor.isBefore(checkOut)) return total;

        long remainderHours = ceilHoursBetween(cursor, checkOut);

        if (overnight != null && remainderHours <= nvl(overnight.getMaxDuration())
                && overnightTimeOk(overnight, cursor, checkOut)) {
            total += overnight.getPrice();
        } else {
            if (combo3h == null || combo5h == null) {
                throw new RuntimeException("Missing short combos (3h/5h) in pricing");
            }
            total += priceShortRemainder(remainderHours, combo3h, combo5h);
        }

        return total;
    }

    private Pricing findByName(List<Pricing> list, String name) {
        return list.stream()
                .filter(p -> p.getComboName() != null && p.getComboName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    private long ceilHoursBetween(LocalDateTime a, LocalDateTime b) {
        long minutes = ChronoUnit.MINUTES.between(a, b);
        return (long) Math.ceil(minutes / 60.0);
    }

    private LocalDateTime min(LocalDateTime a, LocalDateTime b) {
        return a.isBefore(b) ? a : b;
    }

    private boolean overnightTimeOk(Pricing p, LocalDateTime in, LocalDateTime out) {
        if (p.getStartTime() == null || p.getEndTime() == null) return false;
        if (!p.getEndTime().isBefore(p.getStartTime())) return false;

        LocalTime inT = in.toLocalTime();
        LocalTime outT = out.toLocalTime();
        return !inT.isBefore(p.getStartTime()) || !outT.isAfter(p.getEndTime());
    }

    private double priceShortRemainder(long hoursLeft, Pricing combo3h, Pricing combo5h) {
        if (hoursLeft <= 0) return 0.0;

        double total = 0.0;

        while (hoursLeft > 7) {
            total += combo5h.getPrice();
            hoursLeft -= 5;
        }

        if (hoursLeft <= 3) {
            total += combo3h.getPrice();
        } else if (hoursLeft == 4) {
            total += combo3h.getPrice() + nvl(combo3h.getExtraHourPrice());
        } else if (hoursLeft == 5) {
            total += combo5h.getPrice();
        } else if (hoursLeft == 6) {
            total += combo5h.getPrice() + nvl(combo5h.getExtraHourPrice());
        } else if (hoursLeft == 7) {
            total += combo5h.getPrice() + 2 * nvl(combo5h.getExtraHourPrice());
        }
        return total;
    }

    private double nvl(Double v) {
        return v == null ? 0.0 : v;
    }

    private int nvl(Integer v) {
        return v == null ? 0 : v;
    }
}
