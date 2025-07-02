package com.improvemyskills.customerservice.service;

import com.improvemyskills.customerservice.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Customer save(Customer customer);
    Optional<Customer> getCustomer(Long customerId);
    List<Customer> getAllCustomers();
}
