package com.nhantd.homestay.service;

import com.nhantd.homestay.model.Booking;
import com.nhantd.homestay.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class MomoService implements PaymentStrategy {

    @Value("${momo.partnerCode}")
    private String partnerCode;

    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secretKey}")
    private String secretKey;

    @Value("${momo.endpoint}")
    private String endpoint;

    @Value("${momo.returnUrl}")
    private String returnUrl;

    @Value("${momo.notifyUrl}")
    private String notifyUrl;

    private final BookingRepository bookingRepository;

    public MomoService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public String createPayment(Long bookingId) throws Exception {
        // lấy booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        long amount = (long) booking.getTotalPrice();

        String orderId = String.valueOf(System.currentTimeMillis());
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderInfo = "Payment for booking " + bookingId;

        // raw string để ký
        String rawHash = "accessKey=" + accessKey +
                "&amount=" + amount +
                "&extraData=" +
                "&ipnUrl=" + notifyUrl +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode +
                "&redirectUrl=" + returnUrl +
                "&requestId=" + requestId +
                "&requestType=captureWallet";

        String signature = hmacSHA256(secretKey, rawHash);

        // body gửi MoMo
        Map<String, Object> data = new HashMap<>();
        data.put("partnerCode", partnerCode);
        data.put("partnerName", "Test");
        data.put("storeId", "MomoTestStore");
        data.put("requestId", requestId);
        data.put("amount", amount);
        data.put("orderId", orderId);
        data.put("orderInfo", orderInfo);
        data.put("redirectUrl", returnUrl);
        data.put("ipnUrl", notifyUrl);
        data.put("lang", "vi");
        data.put("extraData", "");
        data.put("requestType", "captureWallet"); // captureWallet, payWithATM, payWithCredit
        data.put("signature", signature);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> res = restTemplate.postForEntity(endpoint, data, Map.class);

        if (res.getBody() != null && res.getBody().get("payUrl") != null) {
            System.out.println("===== PAY CREATE =====");
            System.out.println(res.getBody().get("payUrl").toString());
            return res.getBody().get("payUrl").toString();
        }
        throw new RuntimeException("Create MoMo payment failed");
    }

    private String hmacSHA256(String key, String data) throws Exception {
        Mac hmac256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmac256.init(secretKeySpec);
        byte[] bytes = hmac256.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hash = new StringBuilder();
        for (byte b : bytes) {
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }
}

