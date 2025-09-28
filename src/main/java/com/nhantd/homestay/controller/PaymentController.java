package com.nhantd.homestay.controller;

import com.nhantd.homestay.enums.BookingStatus;
import com.nhantd.homestay.repository.BookingRepository;
import com.nhantd.homestay.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final BookingRepository bookingRepository;

    public PaymentController(PaymentService paymentService, BookingRepository bookingRepository) {
        this.paymentService = paymentService;
        this.bookingRepository = bookingRepository;
    }

    /**
     * API tạo link thanh toán VNPay
     */
    @GetMapping("/vnpay")
    public ResponseEntity<String> createPayment(@RequestParam Long bookingId) {
        try {
            String url = paymentService.createPaymentUrl(bookingId);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * API callback khi VNPay redirect về sau khi thanh toán
     */
    @GetMapping("/vnpay-return")
    public ResponseEntity<String> vnpayReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String name = params.nextElement();
            String value = request.getParameter(name);
            if (value != null && !value.isEmpty()) {
                fields.put(name, value);
            }
        }

        String vnp_SecureHash = fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        boolean valid = paymentService.verifyPayment(fields, vnp_SecureHash);
        if (valid && "00".equals(fields.get("vnp_ResponseCode"))) {
            Long bookingId = Long.valueOf(fields.get("vnp_TxnRef"));
            bookingRepository.findById(bookingId).ifPresent(b -> {
                b.setStatus(BookingStatus.PAID);
                bookingRepository.save(b);
            });
            return ResponseEntity.ok("Payment success for booking " + bookingId);
        }

        return ResponseEntity.ok("Payment failed");
    }
}
