package com.nhantd.homestay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UpdateCustomerRequest {
    private String fullName;
    private String idCard;
    private Date dob;
    private String gender;
    private String phone;
}
