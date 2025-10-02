package com.nhantd.homestay.common;

import com.nhantd.homestay.service.MomoService;
import com.nhantd.homestay.service.PayPalService;
import com.nhantd.homestay.service.PaymentStrategy;
import com.nhantd.homestay.service.VNPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFactory {
    private final PayPalService payPalService;
    private final MomoService momoService;
    private final VNPayService vNPayService;

    public PaymentStrategy getPaymentStrategy(String method) {
        return switch (method.toLowerCase()) {
            case "momo" -> momoService;
            case "paypal" -> payPalService;
            case "vnpay" -> vNPayService;
            default -> throw new IllegalArgumentException("Invalid method" + method);
        };
    }
}
