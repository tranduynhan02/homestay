package com.nhantd.homestay.service;

import com.nhantd.homestay.util.CurrencyUtil;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayPalService {

    private final PayPalHttpClient payPalHttpClient;
    private final CurrencyUtil currency;

    @Value("${paypal.returnUrl}")
    private String returnUrl;

    @Value("${paypal.cancelUrl}")
    private String cancelUrl;

    /**
     * Tạo order PayPal
     */
    public String createPayment(Long bookingId, double amountVND) throws IOException {
        double amount = currency.vndToUsd(amountVND);
        OrdersCreateRequest request = new OrdersCreateRequest();
        request.prefer("return=representation");
        request.requestBody(buildOrderRequest(amount, bookingId));

        HttpResponse<Order> response = payPalHttpClient.execute(request);
        if (response.statusCode() == 201) {
            for (LinkDescription link : response.result().links()) {
                if ("approve".equalsIgnoreCase(link.rel())) {
                    System.out.println(link.href());
                    return link.href(); // Link redirect khách hàng tới
                }
            }
        }
        throw new RuntimeException("PayPal order creation failed");
    }

    /**
     * Capture order sau khi khách hàng approve
     */
    public Order capturePayment(String orderId) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        request.requestBody(new OrderRequest());

        HttpResponse<Order> response = payPalHttpClient.execute(request);
        return response.result();
    }

    private OrderRequest buildOrderRequest(double amount, Long bookingId) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl);
        orderRequest.applicationContext(applicationContext);

        PurchaseUnitRequest purchaseUnit = new PurchaseUnitRequest()
                .referenceId(String.valueOf(bookingId))
                .amountWithBreakdown(new AmountWithBreakdown()
                        .currencyCode("USD")
                        .value(String.format("%.2f", amount)));
        orderRequest.purchaseUnits(List.of(purchaseUnit));

        return orderRequest;
    }
}

