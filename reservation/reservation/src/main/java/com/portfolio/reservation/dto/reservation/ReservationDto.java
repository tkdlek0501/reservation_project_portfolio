package com.portfolio.reservation.dto.reservation;

import com.portfolio.reservation.domain.reservation.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    private ReservationStatus status;

    private String name;

//    private String phone;

    private int persons;

    private LocalDateTime requestDate;

    private Long reservationId;
}
