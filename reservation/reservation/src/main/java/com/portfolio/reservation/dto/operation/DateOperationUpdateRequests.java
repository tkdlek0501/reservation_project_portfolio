package com.portfolio.reservation.dto.operation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateOperationUpdateRequests {

    private List<DateOperationUpdateRequest> dateOperations = new ArrayList<>();
}
