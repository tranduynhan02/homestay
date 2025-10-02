package com.nhantd.homestay.service;

public interface PaymentStrategy {
    String createPayment(Long bookingId) throws Exception;
}
