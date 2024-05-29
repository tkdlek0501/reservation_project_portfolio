package com.portfolio.reservation.dto.operation;

import com.portfolio.reservation.domain.schedule.Schedule;
import com.portfolio.reservation.domain.schedule.type.SameDayApprovalType;
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
public class ScheduleResponse {

    private int requestMaxPerson;

    private int requestMinPerson;

    private SameDayApprovalType sameDayRequestApproval;

    private SameDayApprovalType sameDayCancelApproval;

    private List<DateOperationResponse> dateOperations = new ArrayList<>();

    public static ScheduleResponse of(Schedule schedule, List<DateOperationResponse> responses) {

        return ScheduleResponse.builder()
                .requestMaxPerson(schedule.getRequestMaxPerson())
                .requestMinPerson(schedule.getRequestMinPerson())
                .sameDayCancelApproval(schedule.getSameDayCancelApproval())
                .sameDayRequestApproval(schedule.getSameDayRequestApproval())
                .dateOperations(responses)
                .build();
    }
}
