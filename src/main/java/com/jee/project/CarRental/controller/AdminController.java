package com.jee.project.CarRental.controller;

import com.jee.project.CarRental.entity.Car.Car;
import com.jee.project.CarRental.entity.Reservation;
import com.jee.project.CarRental.repository.CarRepository;
import com.jee.project.CarRental.repository.ReservationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final ReservationRepo reservationRepo;
    private final CarRepository carRepository;

    @GetMapping("/reservations")
    public ResponseEntity<?> getReservations(){
        try {
            List<Reservation> reservationList = reservationRepo.findAll();
            List<Map<String, Object>> response = new ArrayList<>();
            for (Reservation r:
                 reservationList) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("car", r.getCar().getTitle());
                responseData.put("customer", r.getCustomer().getFirstName()+" "+r.getCustomer().getLastName());
                responseData.put("email", r.getCustomer().getEmail());
                responseData.put("pick", r.getPickUpDate());
                responseData.put("drop", r.getDropOffDate());
                response.add(responseData);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/new-car")
    public ResponseEntity<?> createCar(@RequestParam("image") MultipartFile image,
                          @RequestParam("title") String title,
                          @RequestParam("description") String description,
                          @RequestParam("price") double price,
                          @RequestParam("category") String category) throws IOException {
        String path = "D:\\Jee-front-end\\public";
        String uniqueFileName = System.currentTimeMillis() + "-" + image.getOriginalFilename();
        String savingPath = path + File.separator + uniqueFileName;

        image.transferTo(new File(savingPath));

        carRepository.save(Car.builder()
                        .title(title)
                        .description(description)
                        .price(price)
                        .Category(category)
                        .reservationRate(0L)
                        .imageUrl(uniqueFileName)
                        .build());

        return ResponseEntity.ok("OK");
    }

    @PutMapping("/edit/{id}")
    public String editCars(@RequestBody Car carReq, @PathVariable Long id){
        Car car = carRepository.findById(id).get();
        car.setCategory(carReq.getCategory());
        car.setPrice(carReq.getPrice());
        car.setDescription(carReq.getDescription());
        car.setTitle(carReq.getTitle());

        carRepository.save(car);

        return "car edited";
    }

    @GetMapping("/getcars")
    public List<Car> getCars(){
        return carRepository.findAll();
    }

    @GetMapping("/{id}")
    public Car getCar(@PathVariable Long id){
        return carRepository.findById(id).get();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCar(@PathVariable Long id){
        carRepository.deleteById(id);
        return "car deleted";
    }

}
