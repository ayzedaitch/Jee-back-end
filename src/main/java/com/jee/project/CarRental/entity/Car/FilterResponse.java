package com.jee.project.CarRental.entity.Car;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class FilterResponse {
    Long id;
    String title;
    String description;
    String imageUrl;
    Double price;
    Long reservationRate;
}
