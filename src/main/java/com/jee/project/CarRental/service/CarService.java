package com.jee.project.CarRental.service;

import com.jee.project.CarRental.entity.Car.BookingRequest;
import com.jee.project.CarRental.entity.Car.Car;
import com.jee.project.CarRental.entity.Car.FilterRequest;
import com.jee.project.CarRental.entity.Car.FilterResponse;
import com.jee.project.CarRental.entity.Customer;
import com.jee.project.CarRental.entity.Reservation;
import com.jee.project.CarRental.repository.CarRepository;
import com.jee.project.CarRental.repository.CustomerRepository;
import com.jee.project.CarRental.repository.ReservationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    @Autowired
    CarRepository carRepository;

    @Autowired
    ReservationRepo reservationRepo;

    @Autowired
    CustomerRepository customerRepository;
    public List<FilterResponse> TopRatedCars(){
        List<FilterResponse> responses = new ArrayList<>();
        for(Car car : carRepository.findFirst6ByOrderByReservationRateDesc()){
            FilterResponse response = new FilterResponse(
                    car.getId(),
                    car.getTitle(),
                    car.getDescription(),
                    car.getImageUrl(),
                    car.getPrice(),
                    car.getReservationRate()
            );
            responses.add(response);
        }
        return  responses;
    }

    public List<FilterResponse> getCars(FilterRequest request) {
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
        List<FilterResponse> responses = new ArrayList<>();
for(Car car : result){
    FilterResponse response = new FilterResponse(
            car.getId(),
            car.getTitle(),
            car.getDescription(),
            car.getImageUrl(),
            car.getPrice(),
            car.getReservationRate()
    );
    responses.add(response);
}
       return  responses;
    }

    public void bookCar(BookingRequest request){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date pickupdate = dateFormat.parse(request.getPickUpDate());
            Date dropoffdate = dateFormat.parse(request.getDropOffDate());
            Reservation reservation = new Reservation();
            reservation.setLocalisation(request.getAdress());
            reservation.setPickUpDate(pickupdate);
            reservation.setDropOffDate(dropoffdate);


            Optional<Car> car = carRepository.findById(request.getId());
            Car finalCar = car.get();
            finalCar.setReservationRate(finalCar.getReservationRate()+1);
            reservation.setCar(finalCar);
            carRepository.save(finalCar);
            Optional<Customer> customer = customerRepository.findByEmail(request.getEmail());
            Customer finalCustomer = customer.get();
            reservation.setCustomer(finalCustomer);

            reservationRepo.save(reservation);


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Car getCar(Long id){
        Optional<Car> car = carRepository.findById(id);
        Car finalCar = car.get();
        return finalCar;
    }
}
