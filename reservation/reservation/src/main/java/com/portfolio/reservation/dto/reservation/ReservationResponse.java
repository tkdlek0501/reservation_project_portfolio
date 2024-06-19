package com.portfolio.reservation.dto.reservation;

import com.portfolio.reservation.domain.reservation.Reservation;
import com.portfolio.reservation.domain.reservation.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {

    @Schema(description = "상태(REQUEST... etc.)", example = "RESERVE_REQUEST")
    private ReservationStatus status;

    @Schema(description = "예약자", example = "홍길동")
    private String name;

    @Schema(description = "전화번호", example = "010xxxxoooo")
    private String phone;

    @Schema(description = "인원", example = "2")
    private int persons;

    @Schema(description = "예약 일자", example = "YYYY-MM-DD HH:mm:SS")
    private LocalDateTime requestDate;

    // 상세보기 - id
    @Schema(description = "상세보기를 위한 예약 id(PK)", example = "1")
    private Long reservationId;

    public static ReservationResponse of(ReservationDto reservation) {

        return ReservationResponse.builder()
                .status(reservation.getStatus())
                .name(reservation.getName())
                .persons(reservation.getPersons())
                .requestDate(reservation.getRequestDate())
                .reservationId(reservation.getReservationId())
                .build();
    }
}
