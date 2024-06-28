package com.portfolio.reservation.controller.operation;

import com.portfolio.reservation.common.CommonResponse;
import com.portfolio.reservation.domain.schedule.type.SameDayApprovalType;
import com.portfolio.reservation.dto.operation.*;
import com.portfolio.reservation.dto.schedule.DateOperationRequest;
import com.portfolio.reservation.dto.schedule.ScheduleCreateRequest;
import com.portfolio.reservation.dto.store.StoreResponse;
import com.portfolio.reservation.service.businessservice.OperationService;
import com.portfolio.reservation.util.SecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Operation Api", description = "예약 설정 관련 API")
@Slf4j
@RestController
@RequestMapping("/v1.0/operations")
@RequiredArgsConstructor
@Validated
public class OperationController {

    private final OperationService operationService;

    @PostMapping("")
    public ResponseEntity<? extends CommonResponse> create(
            @Valid @RequestBody DateOperationRequest request
    ) {

        operationService.createOperation(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @GetMapping("")
    public ResponseEntity<? extends CommonResponse<ScheduleResponse>> getScheduleOperation() {

        return ResponseEntity.ok(new CommonResponse<>(operationService.getScheduleOperation()));
    }

    @PutMapping("")
    public ResponseEntity<? extends CommonResponse> updateDateOperation(
            @Valid @RequestBody DateOperationUpdateRequests request
    ) {

        operationService.updateDateOperation(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<? extends CommonResponse> deleteDateOperation(
            @PathVariable("id") Long id
    ) {

        operationService.deleteDateOperation(id);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @GetMapping("/time-table")
    public ResponseEntity<? extends CommonResponse<List<DateTableResponse>>> getTimeTable(
            @ModelAttribute SearchTimeTableRequest request
    ) {

        return ResponseEntity.ok(new CommonResponse<>(operationService.getTimeTable(request)));
    }

    @PatchMapping("/limit-count")
    public ResponseEntity<? extends CommonResponse> updateDateOperation(
            @Valid @RequestBody ScheduleLimitCountRequest request
    ) {

        operationService.updateScheduleLimitCount(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @PatchMapping("/limit-count")
    public ResponseEntity<? extends CommonResponse> updateDateOperation(
            @Valid @RequestBody ScheduleSameDayRequest request
    ) {

        operationService.updateScheduleSameDay(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @PatchMapping("/time-table")
    public ResponseEntity<? extends CommonResponse> updateDateOperation(
            @Valid @RequestBody TimeTablesRequest request
    ) {

        operationService.updateTimeTable(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }
}
