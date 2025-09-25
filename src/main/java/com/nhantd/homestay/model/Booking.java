package com.nhantd.homestay.model;

import com.nhantd.homestay.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;
    private String guestName;

    @Column(name = "guest_id_card")
    private String idCard;

    @Temporal(TemporalType.DATE)
    private Date dob;

    private String gender;
    private String guestPhone;
    private String guestEmail;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.UNPAID;

    private double totalPrice;
}
