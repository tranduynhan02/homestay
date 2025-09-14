package com.nhantd.homestay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomRequest {
    private String room_name;
    private String description;
    private Long type_id;
    private Long branch_id;
    private Boolean hasBathtub;
    private Boolean hasBalcony;
    private Boolean hasKitchen;
}
