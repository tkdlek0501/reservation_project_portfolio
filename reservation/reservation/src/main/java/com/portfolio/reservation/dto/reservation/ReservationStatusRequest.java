package com.portfolio.reservation.dto.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ReservationStatusRequest {

    @Schema(description = "적용할 id", example = "Long Array")
    private List<Long> reservationIds = new ArrayList<>();

    @Schema(description = "사유", example = "매장 사정으로 인해 취소됐습니다. ...")
    private String reason;
}
