package com.portfolio.reservation.dto.reservation;

import com.portfolio.reservation.domain.reservation.ReservationHistory;
import com.portfolio.reservation.domain.reservation.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationHistoryResponse {

    private Long reservationHistoryId;

    private Long timeTableId;

    private ReservationStatus status;

    private LocalDateTime requestDateTime;

    private int persons;

    private String reserveRequest;

    private LocalDateTime createdAt;

    public static ReservationHistoryResponse of(ReservationHistory history) {

        return ReservationHistoryResponse.builder()
                .reservationHistoryId(history.getId())
                .timeTableId(history.getTimeTableId())
                .status(history.getStatus())
                .requestDateTime(LocalDateTime.of(history.getDate(), history.getTime()))
                .persons(history.getPersons())
                .reserveRequest(history.getReserveRequest())
                .createdAt(history.getCreatedAt())
                .build();
    }
}
