package com.nhantd.homestay.service;

import com.nhantd.homestay.common.PaymentFactory;
import com.nhantd.homestay.model.Booking;
import com.nhantd.homestay.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final BookingRepository bookingRepository;
    private final PaymentFactory paymentFactory;

    public String createPayment(Long id, String method) throws Exception {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));

        PaymentStrategy strategy = paymentFactory.getPaymentStrategy(method);
        return strategy.createPayment(id);
    }
}
