package com.nhantd.homestay.repository;

import com.nhantd.homestay.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerId(Long customerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.room.id = :roomId " +
            "AND DATE(b.checkIn) = :date")
    List<Booking> findByRoomAndDate(@Param("roomId") Long roomId,
                                    @Param("date") LocalDate date);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.room.id = :roomId " +
            "AND b.checkIn < :checkOut " +
            "AND b.checkOut > :checkIn")
    List<Booking> findConflicts(@Param("roomId") Long roomId,
                                @Param("checkIn") LocalDateTime checkIn,
                                @Param("checkOut") LocalDateTime checkOut);
}
