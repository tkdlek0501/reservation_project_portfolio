package com.portfolio.reservation.service.businessservice;

import com.portfolio.reservation.domain.reservation.Reservation;
import com.portfolio.reservation.domain.reservation.ReservationStatus;
import com.portfolio.reservation.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<Reservation> getActive(List<Long> timeTableIds) {

        return reservationRepository.getActive(timeTableIds, ReservationStatus.getImpossibleReservations());
    }

    // TODO: 해당일의 타임 테이블 조회 -> timeTableService

    // TODO: 예약 가능 일자 조회 -> dateTableService

    // TODO: 예약 가능 시간 조회 -> timeTableService
}
