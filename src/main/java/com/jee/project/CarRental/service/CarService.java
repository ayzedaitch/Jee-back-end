package com.jee.project.CarRental.service;

import com.jee.project.CarRental.entity.Car.Car;
import com.jee.project.CarRental.entity.Car.FilterRequest;
import com.jee.project.CarRental.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {
    @Autowired
    CarRepository carRepository;

    public List<Car> TopRatedCars(){
        return carRepository.findFirst8ByOrderByReservationRateDesc();
    }

    public List<Car> getCars(FilterRequest request) {
        String category;
        List<Car> result = new ArrayList<>();

        if(request.getCategory().equals("All Categories")){
            category = null;
        }else {
            category = request.getCategory();
        }

        if(!request.getPrice().equals("Default") && request.getReservation().equals("Default")){
            if (request.getPrice().equals("Low-High")) {
                result = carRepository.filterCars(category, Sort.by(Sort.Direction.ASC, "price"));
            } else if (request.getPrice().equals("High-Low")) {
                result = carRepository.filterCars(category, Sort.by(Sort.Direction.DESC, "price"));
            }
        }else if(!request.getReservation().equals("Default") && request.getPrice().equals("Default")) {
            if (request.getReservation().equals("Most reserved")) {
                result = carRepository.filterCars(category,Sort.by(Sort.Direction.DESC, "reservationRate"));
            }else if (request.getReservation().equals("Least reserved")) {
                result = carRepository.filterCars(category,Sort.by(Sort.Direction.ASC, "reservationRate"));
            }
        }else if(!request.getReservation().equals("Default") && !request.getPrice().equals("Default")) {
            if (request.getPrice().equals("Low-High") && request.getReservation().equals("Most reserved")) {
                result = carRepository.filterCars(category,Sort.by(Sort.Direction.ASC, "price").and(Sort.by(Sort.Direction.DESC, "reservationRate")));
            }else if (request.getPrice().equals("High-Low") && request.getReservation().equals("Most reserved")) {
                result = carRepository.filterCars(category,Sort.by(Sort.Direction.DESC, "price").and(Sort.by(Sort.Direction.DESC, "reservationRate")));
            }else if (request.getPrice().equals("Low-High") && request.getReservation().equals("Least reserved")) {
                result = carRepository.filterCars(category,Sort.by(Sort.Direction.ASC, "price").and(Sort.by(Sort.Direction.ASC, "reservationRate")));
            }else {
                result =carRepository.filterCars(category,Sort.by(Sort.Direction.DESC, "price").and(Sort.by(Sort.Direction.ASC, "reservationRate")));
            }
        }else {
            result = carRepository.filterCarsDefault(category);
        }

       return  result;
    }
}
