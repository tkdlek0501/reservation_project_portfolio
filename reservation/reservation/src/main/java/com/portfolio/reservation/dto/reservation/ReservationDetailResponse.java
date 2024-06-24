package com.portfolio.reservation.dto.reservation;

import com.portfolio.reservation.domain.reservation.ReservationHistory;
import com.portfolio.reservation.domain.reservation.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetailResponse {

    private Long reservationId;

    private Long userId;

    private String nickname;

    private Long timeTableId;

    private ReservationStatus status;

    private LocalDateTime requestDateTime;

    private int persons;

    private String lastReason;

    private LocalDateTime createdAt;

    List<ReservationHistoryResponse> reservationHistories = new ArrayList<>();

    public static ReservationDetailResponse of(
            ReservationDetailDto dto,
            List<ReservationHistory> histories
    ) {
        List<ReservationHistoryResponse> reservationHistories = histories.stream()
                .map(ReservationHistoryResponse::of)
                .toList();

        return ReservationDetailResponse.builder()
                .reservationId(dto.getId())
                .userId(dto.getUserId())
                .nickname(dto.getNickname())
                .timeTableId(dto.getTimeTableId())
                .status(dto.getStatus())
                .requestDateTime(dto.getRequestDateTime())
                .persons(dto.getPersons())
                .lastReason(dto.getLastReason())
                .createdAt(dto.getCreatedAt())
                .reservationHistories(reservationHistories)
                .build();
    }
}
