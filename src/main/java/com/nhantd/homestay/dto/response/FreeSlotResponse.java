package com.nhantd.homestay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FreeSlotResponse {
    private Long roomId;
    private String roomName;
    private LocalDateTime start;
    private LocalDateTime end;
}
