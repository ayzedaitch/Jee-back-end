package com.jee.project.CarRental.controller;

import com.jee.project.CarRental.entity.Car.BookingRequest;
import com.jee.project.CarRental.entity.Car.FilterRequest;
import com.jee.project.CarRental.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class CarController {
    @Autowired
    CarService carService;

    @GetMapping("/topCars")
    public ResponseEntity<?> getTopCars(){

        try {
            return ResponseEntity.ok(carService.TopRatedCars());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/Car/{id}")
    public ResponseEntity<?> getCar(@PathVariable Long id){
        try {
            return ResponseEntity.ok(carService.getCar(id));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
@PostMapping("/filterCars")
    public ResponseEntity<?> Cars(@RequestBody FilterRequest request){
        try {
            return ResponseEntity.ok(carService.getCars(request));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/bookingRequest")
    public ResponseEntity<?> Cars(@RequestBody BookingRequest request){

        try {
            carService.bookCar(request);
            return ResponseEntity.ok("successfully booked");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
