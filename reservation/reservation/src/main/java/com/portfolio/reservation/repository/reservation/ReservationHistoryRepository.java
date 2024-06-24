package com.portfolio.reservation.repository.reservation;

import com.portfolio.reservation.domain.reservation.ReservationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, Long> {


    List<ReservationHistory> findByReservationId(Long reservationId);
}
