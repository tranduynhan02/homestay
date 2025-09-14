package com.nhantd.homestay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int statusCode;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(int statusCode, String error, String message) {
        this.statusCode = statusCode;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
