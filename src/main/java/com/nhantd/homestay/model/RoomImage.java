package com.nhantd.homestay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room_images")
public class RoomImage {
    @Id
    private Long id;
    private String image;

//    @ManyToOne
//    @JoinColumn(name = "room_id", nullable = false)
//    private Room room;

}
