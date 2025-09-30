package com.nhantd.homestay.service;

import com.nhantd.homestay.dto.request.LoginRequest;
import com.nhantd.homestay.dto.request.RegisterRequest;
import com.nhantd.homestay.dto.request.UpdateCustomerRequest;
import com.nhantd.homestay.dto.response.AuthResponse;
import com.nhantd.homestay.enums.Role;
import com.nhantd.homestay.model.Customer;
import com.nhantd.homestay.model.ResetPassword;
import com.nhantd.homestay.model.User;
import com.nhantd.homestay.repository.CustomerRepository;
import com.nhantd.homestay.repository.ResetPasswordRepository;
import com.nhantd.homestay.repository.UserRepository;
import com.nhantd.homestay.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ResetPasswordRepository resetPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // ==========================
    // AUTHENTICATION
    // ==========================

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }

    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = UUID.randomUUID().toString();

        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setToken(token);
        resetPassword.setUser(user);
        resetPassword.setExpiryDate(LocalDateTime.now().plusHours(1));
        resetPasswordRepository.save(resetPassword);

        // TODO: gửi email, hiện demo in ra console
        System.out.println("Reset password link: http://localhost:8080/auth/reset-password?token=" + token);
        return token;
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        ResetPassword resetPassword = resetPasswordRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetPassword.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = resetPassword.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetPasswordRepository.delete(resetPassword);
    }

    // ==========================
    // CUSTOMER PROFILE
    // ==========================

    public Customer updateCustomer(Authentication authentication, UpdateCustomerRequest request) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Customer customer = user.getCustomer();
        if (customer == null) {
            customer = new Customer();
            customer.setUser(user);
        }

        customer.setFullName(request.getFullName());
        customer.setIdCard(request.getIdCard());
        customer.setDob(request.getDob());
        customer.setGender(request.getGender());
        customer.setPhone(request.getPhone());

        return customerRepository.save(customer);
    }

    public Customer updateCustomerByAdmin(Long userId, UpdateCustomerRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Customer customer = user.getCustomer();
        if (customer == null) {
            customer = new Customer();
            customer.setUser(user);
        }

        customer.setFullName(request.getFullName());
        customer.setIdCard(request.getIdCard());
        customer.setDob(request.getDob());
        customer.setGender(request.getGender());
        customer.setPhone(request.getPhone());

        return customerRepository.save(customer);
    }

    // ==========================
    // SPRING SECURITY HOOK
    // ==========================

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }
}
