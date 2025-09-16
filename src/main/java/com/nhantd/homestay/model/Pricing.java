package com.nhantd.homestay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pricing")
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private String comboName;

    @Column(nullable = false)
    private double price;

    private int minDuration;
    private int maxDuration;

    private LocalTime startTime;
    private LocalTime endTime;

    private Boolean weekendOnly = false;
    private Boolean holidayOnly = false;

    private double extraHourPrice;
}
