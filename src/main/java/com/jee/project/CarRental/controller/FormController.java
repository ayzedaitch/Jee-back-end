package com.jee.project.CarRental.controller;

import com.jee.project.CarRental.entity.Customer;
import com.jee.project.CarRental.repository.CustomerRepository;
import com.jee.project.CarRental.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/form")
public class FormController {
    private final CustomerRepository customerRepository;
    private final KeycloakService keycloakService;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Customer customer){
        try {
            keycloakService.registerCustomer(customer);
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customerRepository.save(customer);
            return ResponseEntity.ok("Customer Created Successfully.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
