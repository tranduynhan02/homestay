package com.nhantd.homestay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BranchRequest {
    private String name;
    private String address;
    private String phone;
}
