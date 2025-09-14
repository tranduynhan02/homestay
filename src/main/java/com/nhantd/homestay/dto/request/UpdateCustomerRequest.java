package com.nhantd.homestay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UpdateCustomerRequest {
    private String full_name;
    private String id_Card;
    private Date dob;
    private String gender;
    private String phone;
}
