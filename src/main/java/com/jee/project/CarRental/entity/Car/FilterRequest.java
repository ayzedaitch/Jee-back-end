package com.jee.project.CarRental.entity.Car;


import jakarta.persistence.Entity;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterRequest {
    String category;
    String price;
    String reservation;
}
