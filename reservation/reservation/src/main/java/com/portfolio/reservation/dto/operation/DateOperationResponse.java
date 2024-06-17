package com.portfolio.reservation.dto.operation;

import com.portfolio.reservation.domain.schedule.DateOperation;
import com.portfolio.reservation.domain.schedule.type.TimeUnitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateOperationResponse {

    private Long dateOperationId;

    private LocalDate startDate;

    private LocalDate endDate;

    private TimeUnitType timeUnit;

    @Builder.Default
    private List<TimeOperationResponse> timeOperations = new ArrayList<>();

    public static DateOperationResponse of(DateOperation dateOperation) {

        List<TimeOperationResponse> timeOperations = new ArrayList<>();
        if(!dateOperation.getTimeOperations().isEmpty()) {
            timeOperations = dateOperation.getTimeOperations()
                    .stream().filter(t -> Objects.isNull(t.getExpiredAt()))
                    .map(TimeOperationResponse::of)
                    .collect(Collectors.toList());
        }

        return DateOperationResponse.builder()
                .dateOperationId(dateOperation.getId())
                .startDate(dateOperation.getStartDate())
                .endDate(dateOperation.getEndDate())
                .timeUnit(dateOperation.getTimeUnit())
                .timeOperations(timeOperations)
                .build();
    }
}
