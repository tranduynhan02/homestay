package com.nhantd.homestay.service;

import com.nhantd.homestay.dto.request.UpdateCustomerRequest;
import com.nhantd.homestay.model.CustomUserDetails;
import com.nhantd.homestay.model.Customer;
import com.nhantd.homestay.model.User;
import com.nhantd.homestay.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer updateCustomer(Authentication authentication, UpdateCustomerRequest request) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();
        Customer customer = user.getCustomer();
        if (customer == null) {
            customer = new Customer();
            customer.setUser(user);
        }

        customer.setFull_name(request.getFull_name());
        customer.setId_Card(request.getId_Card());
        customer.setDob(request.getDob());
        customer.setGender(request.getGender());
        customer.setPhone(request.getPhone());

        return customerRepository.save(customer);
    }

    public Customer updateCustomerByAdmin(Long userId, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setFull_name(request.getFull_name());
        customer.setId_Card(request.getId_Card());
        customer.setDob(request.getDob());
        customer.setGender(request.getGender());
        customer.setPhone(request.getPhone());

        return customerRepository.save(customer);
    }
}
