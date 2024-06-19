package com.portfolio.reservation.dto.reservation;

import com.portfolio.reservation.domain.reservation.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SearchReservationRequest {

    @Schema(description = "검색 시작일", example = "YYYY-MM-DD")
    private LocalDate startDate;

    @Schema(description = "검색 종료일", example = "YYYY-MM-DD")
    private LocalDate endDate;

    @Schema(description = "예약 상태")
    private List<ReservationStatus> statuses = new ArrayList<>();

    @Schema(description = "검색어 타입을 입력하시면 됩니다.(NAME or NO or PHONE)", example = "NAME")
    private ReservationKeywordType keywordType;

    @Schema(description = "검색어", example = "홍길동")
    private String keyword = "";
}
