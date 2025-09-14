package com.nhantd.homestay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {
    private Long id;
    private String room_name;
    private String description;
    private Boolean available;
    private String branch_name;
    private String type_name;
    private Boolean hasBathtub;
    private Boolean hasBalcony;
    private Boolean hasKitchen;
}
