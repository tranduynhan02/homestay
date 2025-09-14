package com.nhantd.homestay.controller;

import com.nhantd.homestay.dto.request.UpdateCustomerRequest;
import com.nhantd.homestay.dto.request.UpdateUserRoleRequest;
import com.nhantd.homestay.dto.response.UserResponse;
import com.nhantd.homestay.model.Customer;
import com.nhantd.homestay.service.CustomerService;
import com.nhantd.homestay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final CustomerService customerService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/update-role")
    public ResponseEntity<String> updateUserRole(@RequestBody UpdateUserRoleRequest request) {
        userService.updateUserRole(request.getUsername(), request.getRole());
        return ResponseEntity.ok("User role updated successfully");
    }

    @PutMapping("/users/{id}/update")
    public ResponseEntity<Customer> updateCustomerByAdmin(
            @PathVariable Long id,
            @RequestBody UpdateCustomerRequest request) {
        return ResponseEntity.ok(customerService.updateCustomerByAdmin(id, request));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
