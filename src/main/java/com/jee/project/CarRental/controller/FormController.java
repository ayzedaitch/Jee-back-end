package com.jee.project.CarRental.controller;

import com.jee.project.CarRental.entity.Customer;
import com.jee.project.CarRental.repository.CustomerRepository;
import com.jee.project.CarRental.service.CarService;
import com.jee.project.CarRental.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/form")
@CrossOrigin("*")
public class FormController {
    private final CustomerRepository customerRepository;
    private final KeycloakService keycloakService;
    private final PasswordEncoder passwordEncoder;
    @Value("${keycloak.client.secret}")
    private String keycloakSecret;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("email") String email,
                                   @RequestParam("password") String password){
        String url = "http://localhost:8080/realms/JEE/protocol/openid-connect/token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> formData = new HashMap<>();
        formData.put("client_id", "jee-presentation");
        formData.put("client_secret", keycloakSecret);
        formData.put("grant_type", "password");
        formData.put("username", email);
        formData.put("password", password);

        StringBuilder requestBody = new StringBuilder();
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            // entry === key-value pair
            requestBody.append(entry.getKey());
            requestBody.append("=");
            requestBody.append(entry.getValue());
            requestBody.append("&");
        }

        // Remove the last "&" character
        requestBody.deleteCharAt(requestBody.length() - 1);

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                throw new RuntimeException("Error");
            }
        } catch (Exception e ){
            return ResponseEntity.ok(e.getMessage());
        }

    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam("token") String jwt){
        String url = "http://localhost:8080/realms/JEE/protocol/openid-connect/token/introspect";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create your form data as a Map
        Map<String, String> formData = new HashMap<>();
        formData.put("client_id", "jee-presentation");
        formData.put("client_secret", keycloakSecret);
        formData.put("token",jwt);

        // Create the request body
        StringBuilder requestBody = new StringBuilder();
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            // entry === key-value pair
            requestBody.append(entry.getKey());
            requestBody.append("=");
            requestBody.append(entry.getValue());
            requestBody.append("&");
        }

        // Remove the last "&" character
        requestBody.deleteCharAt(requestBody.length() - 1);

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                throw new RuntimeException("Error");
            }
        } catch (Exception e ){
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam("refresh_token") String refresh){
        String url = "http://localhost:8080/realms/JEE/protocol/openid-connect/logout";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create your form data as a Map
        Map<String, String> formData = new HashMap<>();
        formData.put("client_id", "jee-presentation");
        formData.put("client_secret", keycloakSecret);
        formData.put("refresh_token",refresh);

        // Create the request body
        StringBuilder requestBody = new StringBuilder();
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            // entry === key-value pair
            requestBody.append(entry.getKey());
            requestBody.append("=");
            requestBody.append(entry.getValue());
            requestBody.append("&");
        }

        // Remove the last "&" character
        requestBody.deleteCharAt(requestBody.length() - 1);

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                throw new RuntimeException("Error");
            }
        } catch (Exception e ){
            return ResponseEntity.ok(e.getMessage());
        }
    }


}
