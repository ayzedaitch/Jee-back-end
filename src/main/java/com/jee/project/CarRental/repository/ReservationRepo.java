package com.jee.project.CarRental.repository;

import com.jee.project.CarRental.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepo extends JpaRepository<Reservation,Long> {

}
