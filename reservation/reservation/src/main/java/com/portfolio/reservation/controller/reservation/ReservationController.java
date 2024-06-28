package com.portfolio.reservation.controller.reservation;

import com.portfolio.reservation.common.CommonResponse;
import com.portfolio.reservation.dto.operation.DateOperationUpdateRequests;
import com.portfolio.reservation.dto.operation.ScheduleResponse;
import com.portfolio.reservation.dto.reservation.*;
import com.portfolio.reservation.dto.schedule.DateOperationRequest;
import com.portfolio.reservation.service.businessservice.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Reservation Api", description = "예약 요청 관련 API")
@Slf4j
@RestController
@RequestMapping("/v1.0/reservations")
@RequiredArgsConstructor
@Validated
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("")
    public ResponseEntity<? extends CommonResponse<AvailableTimeDto>> getAvailableReservation(
            @RequestParam("scheduleId") Long scheduleId,
            @RequestParam("date") String date,
            @RequestParam("reservationId") Long reservationId
    ) {

        return ResponseEntity.ok(new CommonResponse<>(reservationService.getAvailableReservation(scheduleId, date, reservationId)));
    }

    @PostMapping("")
    public ResponseEntity<? extends CommonResponse> create(
            @Valid @RequestBody ReservationCreateRequest request
    ) {

        reservationService.createUserReservation(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @PutMapping("")
    public ResponseEntity<? extends CommonResponse> updateUserReservation(
            @Valid @RequestBody ReservationModifyRequest request
    ) {

        reservationService.updateUserReservation(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<? extends CommonResponse> deleteReservation(
            @Valid @RequestBody ReservationCancelRequest request
    ) {

        reservationService.deleteReservation(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @GetMapping("/search")
    public ResponseEntity<? extends CommonResponse<Page<ReservationResponse>>> findReservation(
            @ModelAttribute SearchReservationRequest request,
            @PageableDefault Pageable pageable
    ) {

        return ResponseEntity.ok(new CommonResponse<>(reservationService.findReservation(request, pageable)));
    }

    @GetMapping("/{id}}")
    public ResponseEntity<? extends CommonResponse<ReservationDetailResponse>> getReservationDetail(
            @PathVariable("id") Long id
    ) {

        return ResponseEntity.ok(new CommonResponse<>(reservationService.getReservationDetail(id)));
    }

    @GetMapping("/me")
    public ResponseEntity<? extends CommonResponse<Page<ReservationUserResponse>>> getMyReservation(
            @ModelAttribute SearchReservationRequest request,
            @PageableDefault Pageable pageable
    ) {

        return ResponseEntity.ok(new CommonResponse<>(reservationService.getMyReservation(request, pageable)));
    }

    @PatchMapping("/change-confirm")
    public ResponseEntity<? extends CommonResponse> confirmChange(
            @Valid @RequestBody ReservationStatusRequest request
    ) {

        reservationService.confirmChange(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @PatchMapping("/change-refuse")
    public ResponseEntity<? extends CommonResponse> refuseChange(
            @Valid @RequestBody ReservationStatusRequest request
    ) {

        reservationService.refuseChange(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @PatchMapping("/store-cancel")
    public ResponseEntity<? extends CommonResponse> cancelByStore(
            @Valid @RequestBody ReservationStatusRequest request
    ) {

        reservationService.cancelByStore(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @PatchMapping("/complete")
    public ResponseEntity<? extends CommonResponse> complete(
            @Valid @RequestBody ReservationStatusRequest request
    ) {

        reservationService.complete(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @PatchMapping("/no-show")
    public ResponseEntity<? extends CommonResponse> updateToNoShow(
            @Valid @RequestBody ReservationStatusRequest request
    ) {

        reservationService.updateToNoShow(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }
}
