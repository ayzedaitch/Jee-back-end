package com.jee.project.CarRental.entity.Car;

import com.jee.project.CarRental.entity.Reservation;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String imageUrl;
    String description;
    Double price;
    Long reservationRate;
    String Category;

//    @OneToOne(mappedBy = "car")
//    Reservation reservation;
}
