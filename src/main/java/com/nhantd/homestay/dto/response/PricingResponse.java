package com.nhantd.homestay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingResponse {
    private Long id;
    private String branchName;
    private String roomTypeName;
    private String comboName;
    private Double price;
    private Integer minDuration;
    private Integer maxDuration;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean weekendOnly;
    private Boolean holidayOnly;
    private Double extraHourPrice;
}
