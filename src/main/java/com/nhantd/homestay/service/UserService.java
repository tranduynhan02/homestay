package com.nhantd.homestay.service;

import com.nhantd.homestay.dto.response.UserResponse;
import com.nhantd.homestay.model.CustomUserDetails;
import com.nhantd.homestay.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public UserResponse getProfile(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        return new UserResponse(userDetails.getUsername(), user.getEmail(), user.getRole());
    }
}
