package com.jee.project.CarRental.entity.Car;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {

    private String pickUpDate;
    private String dropOffDate;
    private String adress;
    private Long id;
    private String email;
}
