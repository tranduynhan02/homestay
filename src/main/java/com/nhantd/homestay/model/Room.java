package com.nhantd.homestay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String room_name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Boolean available = true;

    @Column(nullable = false)
    private Boolean hasBathtub = false;

    @Column(nullable = false)
    private Boolean hasBalcony = false;

    @Column(nullable = false)
    private Boolean hasKitchen = false;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private RoomType type;

//    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<RoomImage> images = new ArrayList<>();

    public Room(String room_name, String description, Branch branch, RoomType type, Boolean hasBathtub, Boolean hasBalcony, Boolean hasKitchen) {
        this.room_name = room_name;
        this.description = description;
        this.branch = branch;
        this.type = type;
        this.hasBathtub = hasBathtub;
        this.hasBalcony = hasBalcony;
        this.hasKitchen = hasKitchen;
        this.available = true;
    }
}
