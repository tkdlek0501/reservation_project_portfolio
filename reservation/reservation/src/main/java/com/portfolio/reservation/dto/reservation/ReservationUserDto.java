package com.portfolio.reservation.dto.reservation;

import com.portfolio.reservation.domain.reservation.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUserDto {

    private ReservationStatus status;

    private Long storeId;

    private String storeName;

    private int persons;

    private LocalDateTime requestDate;

    private Long reservationId;

    private String lastReason;
}

