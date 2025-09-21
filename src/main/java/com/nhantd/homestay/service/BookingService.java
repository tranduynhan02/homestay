package com.nhantd.homestay.service;

import com.nhantd.homestay.dto.request.BookingRequest;
import com.nhantd.homestay.dto.response.BookingResponse;
import com.nhantd.homestay.enums.BookingStatus;
import com.nhantd.homestay.model.Booking;
import com.nhantd.homestay.model.Customer;
import com.nhantd.homestay.model.Room;
import com.nhantd.homestay.repository.BookingRepository;
import com.nhantd.homestay.repository.CustomerRepository;
import com.nhantd.homestay.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final CustomerRepository customerRepository;
    private final PricingService pricingService;

    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<BookingResponse> getBookingsByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId).stream()
                .map(this::toResponse)
                .toList();
    }

    public BookingResponse updateStatus(Long id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(status);
        return toResponse(bookingRepository.save(booking));
    }

    public BookingResponse createBooking(Long customerId, BookingRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        List<Booking> conflicts = bookingRepository.findConflicts(
                room.getId(), request.getCheckIn(), request.getCheckOut());
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Room is already booked in this time range");
        }

        double price = pricingService.calculatePrice(
                room.getBranch(),
                room.getType(),
                request.getCheckIn(),
                request.getCheckOut()
        );

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        booking.setStatus(BookingStatus.UNPAID);
        booking.setTotalPrice(price);

        if (customerId != null) {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            booking.setCustomer(customer);
        } else {
            booking.setGuestName(request.getGuestName());
            booking.setIdCard(request.getIdCard());
            booking.setDob(request.getDob());
            booking.setGender(request.getGender());
            booking.setGuestPhone(request.getGuestPhone());
            booking.setGuestEmail(request.getGuestEmail());
        }

        return toResponse(bookingRepository.save(booking));
    }

    public BookingResponse updateBooking(Long bookingId, BookingRequest request, Long customerId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Long targetRoomId = request.getRoomId() != null ? request.getRoomId() : booking.getRoom().getId();
        LocalDateTime newCheckIn = request.getCheckIn() != null ? request.getCheckIn() : booking.getCheckIn();
        LocalDateTime newCheckOut = request.getCheckOut() != null ? request.getCheckOut() : booking.getCheckOut();

        List<Booking> conflicts = bookingRepository.findConflicts(targetRoomId, newCheckIn, newCheckOut)
                .stream()
                .filter(b -> !b.getId().equals(bookingId))
                .toList();

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Room is already booked in this time range");
        }

        if (request.getRoomId() != null) {
            Room room = roomRepository.findById(request.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Room not found"));
            booking.setRoom(room);
        }

        if (request.getCheckIn() != null) booking.setCheckIn(request.getCheckIn());
        if (request.getCheckOut() != null) booking.setCheckOut(request.getCheckOut());

        double price = pricingService.calculatePrice(
                booking.getRoom().getBranch(),
                booking.getRoom().getType(),
                booking.getCheckIn(),
                booking.getCheckOut()
        );
        booking.setTotalPrice(price);

        if (booking.getCustomer() == null) {
            booking.setGuestName(request.getGuestName());
            booking.setIdCard(request.getIdCard());
            booking.setDob(request.getDob());
            booking.setGender(request.getGender());
            booking.setGuestPhone(request.getGuestPhone());
            booking.setGuestEmail(request.getGuestEmail());
        } else {
            if (customerId != null) {
                Customer customer = customerRepository.findById(customerId)
                        .orElseThrow(() -> new RuntimeException("Customer not found"));
                booking.setCustomer(customer);
            }
        }

        return toResponse(bookingRepository.save(booking));
    }

    public Map<LocalDate, List<FreeSlotResponse>> getWeeklyAvailability(Long branchId) {
        List<Room> rooms = roomRepository.findByBranchId(branchId);
        Map<LocalDate, List<FreeSlotResponse>> calendar = new LinkedHashMap<>();

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(7);

        for (LocalDate date = today; !date.isAfter(endDate); date = date.plusDays(1)) {
            List<FreeSlotResponse> freeSlotsForDay = new ArrayList<>();

            for (Room room : rooms) {
                List<Booking> bookings = bookingRepository.findByRoomAndDate(room.getId(), date);
                bookings.sort(Comparator.comparing(Booking::getCheckIn));

                LocalTime lastEnd = LocalTime.MIDNIGHT;

                for (Booking b : bookings) {
                    LocalTime start = b.getCheckIn().toLocalTime();
                    if (start.isAfter(lastEnd)) {
                        freeSlotsForDay.add(new FreeSlotResponse(
                                room.getId(),
                                room.getRoomName(),
                                date.atTime(lastEnd),
                                date.atTime(start)
                        ));
                    }
                    lastEnd = b.getCheckOut().toLocalTime();
                }

                if (lastEnd.isBefore(LocalTime.MIDNIGHT.plusHours(24))) {
                    freeSlotsForDay.add(new FreeSlotResponse(
                            room.getId(),
                            room.getRoomName(),
                            date.atTime(lastEnd),
                            date.atTime(23, 59)
                    ));
                }
            }

            calendar.put(date, freeSlotsForDay);
        }

        return calendar;
    }

    private BookingResponse toResponse(Booking booking) {
        BookingResponse dto = new BookingResponse();
        dto.setId(booking.getId());
        dto.setRoomNumber(booking.getRoom().getRoom_name());
        dto.setCheckIn(booking.getCheckIn());
        dto.setCheckOut(booking.getCheckOut());
        dto.setStatus(booking.getStatus());
        dto.setTotalPrice(booking.getTotalPrice());

        if (booking.getCustomer() != null) {
            dto.setCustomerName(booking.getCustomer().getFull_name());
        } else {
            dto.setGuestName(booking.getGuestName());
            dto.setIdCard(booking.getIdCard());
            dto.setDob(booking.getDob());
            dto.setGender(booking.getGender());
            dto.setGuestPhone(booking.getGuestPhone());
            dto.setGuestEmail(booking.getGuestEmail());
        }

        return dto;
    }
}
