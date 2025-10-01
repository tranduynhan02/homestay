package com.nhantd.homestay.controller;

import com.nhantd.homestay.enums.BookingStatus;
import com.nhantd.homestay.model.Booking;
import com.nhantd.homestay.repository.BookingRepository;
import com.nhantd.homestay.service.MomoService;
import com.nhantd.homestay.service.PayPalService;
import com.nhantd.homestay.service.PaymentService;
import com.paypal.orders.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final MomoService momoService;
    private final PayPalService payPalService;
    private final BookingRepository bookingRepository;

    /**
     * API tạo link thanh toán VNPay
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/vnpay")
    public ResponseEntity<String> createVNPay(@RequestParam Long bookingId) {
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
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
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

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/momo")
    public ResponseEntity<String> createPayment(@RequestParam Long bookingId,
                                                @RequestParam(defaultValue = "captureWallet") String method) {
        try {
            String url = momoService.createPayment(bookingId, method);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/momo-return")
    public ResponseEntity<String> momoReturn(@RequestParam Map<String, String> params) {
        System.out.println("===== MOMO RETURN =====");
        params.forEach((k, v) -> System.out.println(k + ": " + v));

        if ("0".equals(params.get("resultCode"))) {
            String orderInfo = params.get("orderInfo");
            if (orderInfo != null && orderInfo.contains("booking")) {
                Long bookingId = Long.parseLong(orderInfo.replace("Payment for booking ", ""));
                bookingRepository.findById(bookingId).ifPresent(b -> {
                    b.setStatus(BookingStatus.PAID);
                    bookingRepository.save(b);
                });
            }
            return ResponseEntity.ok("✅ Payment success for " + params.get("orderInfo"));
        }
        return ResponseEntity.ok("❌ Payment failed");
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/momo-ipn")
    public ResponseEntity<String> momoIpn(@RequestBody Map<String, Object> body) {
        System.out.println("===== MOMO IPN =====");
        body.forEach((k, v) -> System.out.println(k + ": " + v));

        if ("0".equals(String.valueOf(body.get("resultCode")))) {
            String orderInfo = (String) body.get("orderInfo");
            if (orderInfo != null && orderInfo.contains("booking")) {
                Long bookingId = Long.parseLong(orderInfo.replace("Payment for booking ", ""));
                bookingRepository.findById(bookingId).ifPresent(b -> {
                    b.setStatus(BookingStatus.PAID);
                    bookingRepository.save(b);
                });
            }
        }

        return ResponseEntity.ok("IPN received");
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/paypal")
    public ResponseEntity<String> createPayment(@RequestParam Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        try {
            String approvalLink = payPalService.createPayment(booking.getId(), booking.getTotalPrice());
            return ResponseEntity.ok(approvalLink);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/paypal-return")
    public ResponseEntity<String> success(@RequestParam("token") String orderId) {
        try {
            Order order = payPalService.capturePayment(orderId);
            bookingRepository.findById(Long.parseLong(order.purchaseUnits().get(0).referenceId()))
                    .ifPresent(b -> {
                        b.setStatus(BookingStatus.PAID);
                        bookingRepository.save(b);
                    });

            return ResponseEntity.ok("✅ Payment success with PayPal. OrderId: " + order.id());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error capturing order: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/paypal-cancel")
    public ResponseEntity<String> cancel() {
        return ResponseEntity.ok("❌ Payment canceled by user");
    }
}
