package com.jee.project.CarRental.service;

import com.jee.project.CarRental.entity.Customer;

import java.util.Optional;

public interface CustomerService {
    Customer findById(Long id);
    Customer findByEmail(String email);
    Customer save(Customer customer);
    String deleteById(Long id);
}
