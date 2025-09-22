package com.nhantd.homestay.service;

import com.nhantd.homestay.dto.request.LoginRequest;
import com.nhantd.homestay.dto.request.RegisterRequest;
import com.nhantd.homestay.dto.response.AuthResponse;
import com.nhantd.homestay.dto.response.UserResponse;
import com.nhantd.homestay.enums.Role;
import com.nhantd.homestay.exception.UserAlreadyExistsException;
import com.nhantd.homestay.model.CustomUserDetails;
import com.nhantd.homestay.model.Customer;
import com.nhantd.homestay.model.User;
import com.nhantd.homestay.repository.CustomerRepository;
import com.nhantd.homestay.repository.UserRepository;
import com.nhantd.homestay.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(RegisterRequest request) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(Role.ROLE_USER);
        User savedUser = userRepository.save(user);
        Customer customer = new Customer();
        customer.setUser(savedUser);
        customerRepository.save(customer);
        return new UserResponse(user.getUsername(), user.getEmail(), user.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        CustomUserDetails userDetails =
                (CustomUserDetails) userDetailsService.loadUserByUsername(request.getUsername());

        String jwt = jwtUtil.generateToken(userDetails);

        return new AuthResponse(jwt);
    }

}
