package com.portfolio.reservation.dto.reservation;

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
public class ReservationUserResponse {

    @Schema(description = "상태(REQUEST... etc.)", example = "RESERVE_REQUEST")
    private ReservationStatus status;

    @Schema(description = "인원", example = "2")
    private int persons;

    @Schema(description = "예약 일자", example = "YYYY-MM-DD HH:mm:SS")
    private LocalDateTime requestDate;

    @Schema(description = "매장 id")
    private Long storeId;

    @Schema(description = "매장명")
    private String storeName;

    // 상세보기 - id
    @Schema(description = "상세보기를 위한 예약 id(PK)", example = "1")
    private Long reservationId;

    @Schema(description = "예약 사유")
    private String lastReason;

    public static ReservationUserResponse of(ReservationUserDto dto) {

        return ReservationUserResponse.builder()
                .status(dto.getStatus())
                .persons(dto.getPersons())
                .requestDate(dto.getRequestDate())
                .storeId(dto.getStoreId())
                .storeName(dto.getStoreName())
                .reservationId(dto.getReservationId())
                .lastReason(dto.getLastReason())
                .build();

    }
}
