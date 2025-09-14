package com.nhantd.homestay.controller;

import com.nhantd.homestay.dto.request.UpdateCustomerRequest;
import com.nhantd.homestay.dto.response.UserResponse;
import com.nhantd.homestay.model.Customer;
import com.nhantd.homestay.service.CustomerService;
import com.nhantd.homestay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CustomerService customerService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getProfile(authentication));
    }

    @PutMapping("/update")
    public ResponseEntity<Customer> updateProfile(Authentication authentication, @RequestBody UpdateCustomerRequest request) {
        return ResponseEntity.ok(customerService.updateCustomer(authentication, request));
    }
}
