package com.nhantd.homestay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    private Long roomId;
    private Long customerId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    private String guestName;
    private String idCard;
    private Date dob;
    private String gender;
    private String guestPhone;
    private String guestEmail;
}
