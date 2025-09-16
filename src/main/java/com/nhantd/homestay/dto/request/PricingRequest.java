package com.nhantd.homestay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingRequest {
    private Long branchId;
    private Long roomTypeId;
    private String comboName;
    private double price;
    private int minDuration;
    private int maxDuration;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean weekendOnly = false;
    private Boolean holidayOnly = false;
    private double extraHourPrice;
}
