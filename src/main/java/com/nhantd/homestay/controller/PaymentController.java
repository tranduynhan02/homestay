package com.nhantd.homestay.controller;

import com.nhantd.homestay.enums.BookingStatus;
import com.nhantd.homestay.repository.BookingRepository;
import com.nhantd.homestay.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final BookingRepository bookingRepository;

    @GetMapping("/vnpay")
    public ResponseEntity<String> createPayment(@RequestParam Long bookingId) throws UnsupportedEncodingException {
        String url = paymentService.createPaymentUrl(bookingId);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<String> vnpayReturn(@RequestParam Map<String, String> params) {
        boolean valid = paymentService.verifyPayment(new HashMap<>(params));
        if (valid && "00".equals(params.get("vnp_ResponseCode"))) {
            Long bookingId = Long.valueOf(params.get("vnp_TxnRef"));
            bookingRepository.findById(bookingId).ifPresent(b -> {
                b.setStatus(BookingStatus.PAID);
                bookingRepository.save(b);
            });
            return ResponseEntity.ok("Payment success for booking " + bookingId);
        }
        return ResponseEntity.ok("Payment failed");
    }
}
