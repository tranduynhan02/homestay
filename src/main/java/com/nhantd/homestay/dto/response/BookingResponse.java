package com.nhantd.homestay.dto.response;

import com.nhantd.homestay.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long id;
    private String customerName;
    private String guestName;
    private String idCard;
    private Date dob;
    private String gender;
    private String guestPhone;
    private String guestEmail;
    private String roomNumber;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private BookingStatus status;
    private Double totalPrice;
}
