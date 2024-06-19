package com.portfolio.reservation.repository.reservation;

import com.portfolio.reservation.domain.reservation.Reservation;
import com.portfolio.reservation.domain.reservation.ReservationStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r" +
            " WHERE r.timeTableId IN :timeTableIds" +
            " AND r.status IN :status" +
            " AND r.expiredAt IS NULL")
    List<Reservation> getActive(
            @Param("timeTableIds") List<Long> timeTableIds,
            @Param("status") List<ReservationStatus> status);

    Optional<Reservation> findById(Long id);
}
