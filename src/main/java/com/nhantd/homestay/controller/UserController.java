package com.nhantd.homestay.controller;

import com.nhantd.homestay.dto.request.UpdateCustomerRequest;
import com.nhantd.homestay.dto.response.UserResponse;
import com.nhantd.homestay.model.Customer;
import com.nhantd.homestay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ==========================
    // AUTH
    // ==========================

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailRequest request) {
        String token = userService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("Password reset link sent! Token: " + token);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token,
                                                @RequestBody PasswordResetRequest request) {
        userService.resetPassword(token, request.getNewPassword());
        return ResponseEntity.ok("Password has been reset successfully");
    }

    // ==========================
    // PROFILE
    // ==========================
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getProfile(authentication));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/profile")
    public ResponseEntity<Customer> updateCustomer(Authentication authentication,
                                                   @RequestBody UpdateCustomerRequest request) {
        return ResponseEntity.ok(userService.updateCustomer(authentication, request));
    }

    // ==========================
    // ADMIN FUNCTIONS
    // ==========================

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{username}/role")
    public ResponseEntity<UserResponse> updateRole(@PathVariable String username,
                                                   @RequestParam Role role) {
        return ResponseEntity.ok(userService.updateUserRole(username, role));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}/customer")
    public ResponseEntity<Customer> updateCustomerByAdmin(@PathVariable Long id,
                                                          @RequestBody UpdateCustomerRequest request) {
        return ResponseEntity.ok(userService.updateCustomerByAdmin(id, request));
    }
}
