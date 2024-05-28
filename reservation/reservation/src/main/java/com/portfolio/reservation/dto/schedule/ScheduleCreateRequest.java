package com.portfolio.reservation.dto.schedule;

import com.portfolio.reservation.domain.schedule.Schedule;
import com.portfolio.reservation.domain.schedule.type.SameDayApprovalType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleCreateRequest {

    private String sameDayRequestApproval; // SameDayApprovalType

    private String sameDayCancelApproval; // SameDayApprovalType

    @NotNull(message = "휴무일 사용 여부를 설정해주세요.")
    private boolean useHoliday;

    @Min(value = 1, message = "한 번에 예약 가능한 최대 인원은 최소 1명 이상이어야 합니다.")
    private int requestMaxPerson; // 1 이상

    @Min(value = 1, message = "한 번에 예약 가능한 최소 인원은 최소 1명 이상이어야 합니다.")
    private int requestMinPerson; // 1 이상
}
