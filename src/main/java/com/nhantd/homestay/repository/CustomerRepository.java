package com.nhantd.homestay.repository;

import com.nhantd.homestay.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

}
