package com.portfolio.reservation.service.reservationhistory;

import com.portfolio.reservation.domain.reservation.ReservationHistory;
import com.portfolio.reservation.repository.reservation.ReservationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationHistoryService {

    private final ReservationHistoryRepository reservationHistoryRepository;

    @Transactional
    public void save(ReservationHistory reservationHistory) {

        reservationHistoryRepository.save(reservationHistory);
    }
}
