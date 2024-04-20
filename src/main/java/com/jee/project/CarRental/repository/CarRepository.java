package com.jee.project.CarRental.repository;

import com.jee.project.CarRental.entity.Car.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car,Long> {
    List<Car> findFirst8ByOrderByReservationRateDesc();
    @Query("SELECT DISTINCT c FROM Car c  WHERE :category IS NULL OR c.Category = :category")
    List<Car> filterCars(
            @Param("category") String category,
            Sort sort
    );
    @Query("SELECT DISTINCT c FROM Car c  WHERE :category IS NULL OR c.Category = :category")
    List<Car> filterCarsDefault(
            @Param("category") String category
    );
}
