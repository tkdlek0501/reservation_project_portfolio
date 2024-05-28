package com.portfolio.reservation.controller.schedule;

import com.portfolio.reservation.common.CommonResponse;
import com.portfolio.reservation.domain.schedule.type.SameDayApprovalType;
import com.portfolio.reservation.domain.user.User;
import com.portfolio.reservation.dto.schedule.ScheduleCreateRequest;
import com.portfolio.reservation.dto.store.StoreCreateRequest;
import com.portfolio.reservation.service.schedule.ScheduleService;
import com.portfolio.reservation.util.SecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Schedule Api", description = "예약 설정 관련 API")
@Slf4j
@RestController
@RequestMapping("/v1.0/schedules")
@RequiredArgsConstructor
@Validated
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("")
    public ResponseEntity<? extends CommonResponse> create(
            @Valid @RequestBody ScheduleCreateRequest request
    ) {

        scheduleService.create(
                SecurityUtil.getLonginUser(),
                SameDayApprovalType.convertStringToEnum(request.getSameDayRequestApproval()),
                SameDayApprovalType.convertStringToEnum(request.getSameDayCancelApproval()),
                request.isUseHoliday(),
                request.getRequestMaxPerson(),
                request.getRequestMinPerson()
                );
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

}
