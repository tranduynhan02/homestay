package com.nhantd.homestay.service;

import com.nhantd.homestay.dto.response.UserResponse;
import com.nhantd.homestay.enums.Role;
import com.nhantd.homestay.model.CustomUserDetails;
import com.nhantd.homestay.model.User;
import com.nhantd.homestay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepository userRepository;

    public UserResponse getProfile(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        return new UserResponse(user.getUsername(), user.getEmail(), user.getRole());
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(user -> new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        )).toList();
    }

    public void updateUserRole(String username, Role role) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found!"));
        user.setRole(role);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}
