package com.nhantd.homestay.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CurrencyUtil {
    @Value("${currency.vnd.usd}")
    private double vndToUsdRate;

    public double vndToUsd(double vnd) {
        double usd = vnd * vndToUsdRate;
        return Math.round(usd * 100.0) / 100.0; // làm tròn 2 số thập phân
    }
}
