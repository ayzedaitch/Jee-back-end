package com.jee.project.CarRental.entity;


import com.jee.project.CarRental.entity.Car.Car;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@RequiredArgsConstructor
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Date pickUpDate;
    Date dropOffDate;
    String localisation;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;

    @OneToOne
    @JoinColumn(name = "car_id")
    Car car;
}
