package com.nhantd.homestay.service;

import com.nhantd.homestay.dto.request.UserRequest;
import com.nhantd.homestay.dto.response.UserResponse;
import com.nhantd.homestay.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(Long id);

    UserResponse createUser(UserRequest userRequest);

    void deleteUser(Long id);

    User findByUsername(String username);
}
