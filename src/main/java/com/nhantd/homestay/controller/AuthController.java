package com.nhantd.homestay.controller;

import com.nhantd.homestay.dto.request.EmailRequest;
import com.nhantd.homestay.dto.request.LoginRequest;
import com.nhantd.homestay.dto.request.RegisterRequest;
import com.nhantd.homestay.dto.request.ResetPasswordRequest;
import com.nhantd.homestay.dto.response.AuthResponse;
import com.nhantd.homestay.dto.response.UserResponse;
import com.nhantd.homestay.exception.UserAlreadyExistsException;
import com.nhantd.homestay.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) throws UserAlreadyExistsException {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("User logged out successfully");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailRequest request) {
        String token = authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("Password reset link sent! Token: " + token);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(token, request.getNewPassword());
        return ResponseEntity.ok("Password reset successful!");
    }
}
