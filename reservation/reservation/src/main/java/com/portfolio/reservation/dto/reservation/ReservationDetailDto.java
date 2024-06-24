package com.portfolio.reservation.dto.reservation;

import com.portfolio.reservation.domain.reservation.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetailDto {

    private Long id; // reservationId

    private Long userId;

    private String nickname;

    private Long timeTableId;

    private ReservationStatus status;

    private LocalDateTime requestDateTime;

    private int persons;

    private String lastReason;

    private LocalDateTime createdAt;
}
