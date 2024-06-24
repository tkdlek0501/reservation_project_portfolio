package com.portfolio.reservation.dto.reservation;

import com.portfolio.reservation.domain.reservation.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationSearchCondition {

    private Long storeId;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<ReservationStatus> statuses = new ArrayList<>();

    private String keyword;

    private ReservationKeywordType keywordType;

    private Long userId;

    public static ReservationSearchCondition of(
            Long storeId,
            LocalDate startDate,
            LocalDate endDate,
            List<ReservationStatus> statuses,
            String keyword,
            ReservationKeywordType keywordType,
            Long userId
    ) {

        return ReservationSearchCondition.builder()
                .storeId(storeId)
                .startDate(startDate)
                .endDate(endDate)
                .statuses(statuses)
                .keyword(keyword)
                .keywordType(keywordType)
                .userId(userId)
                .build();
    }
}
