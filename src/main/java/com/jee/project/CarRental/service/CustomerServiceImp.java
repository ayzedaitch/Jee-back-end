package com.jee.project.CarRental.service;

import com.jee.project.CarRental.entity.Customer;
import com.jee.project.CarRental.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService{
    private final CustomerRepository customerRepository;

    @Override
    public Customer findById(Long id) {
        if (customerRepository.findById(id).isEmpty()){
            throw new RuntimeException("Customer Not Found");
        }
        return customerRepository.findById(id).get();
    }

    @Override
    public Customer findByEmail(String email) {
        if (customerRepository.findByEmail(email).isEmpty()){
            throw new RuntimeException("Customer Not Found");
        }
        return customerRepository.findByEmail(email).get();
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public String deleteById(Long id) {
        if (customerRepository.findById(id).isEmpty()){
            throw new RuntimeException("Customer Not Found");
        }
        return "Customer with id: "+id+" was deleted successfully.";
    }
}
