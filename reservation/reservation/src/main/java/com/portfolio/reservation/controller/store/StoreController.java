package com.portfolio.reservation.controller.store;

import com.portfolio.reservation.common.CommonResponse;
import com.portfolio.reservation.dto.store.StoreCreateRequest;
import com.portfolio.reservation.dto.store.StoreResponse;
import com.portfolio.reservation.dto.store.StoreUpdateRequest;
import com.portfolio.reservation.dto.user.UserPasswordUpdateRequest;
import com.portfolio.reservation.service.store.StoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.DeleteExchange;

@Tag(name = "Store Api", description = "매장 관련 API")
@Slf4j
@RestController
@RequestMapping("/v1.0/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping("")
    public ResponseEntity<? extends CommonResponse> create(
            @RequestBody StoreCreateRequest request
    ) {

        storeService.create(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @PatchMapping("")
    public ResponseEntity<? extends CommonResponse> update(
            @RequestBody StoreUpdateRequest request
    ) {

        storeService.update(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @DeleteMapping("")
    public ResponseEntity<? extends CommonResponse> delete(
            @RequestBody UserPasswordUpdateRequest request
    ) {

        storeService.delete(request.getCheckPassword());
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @GetMapping("")
    public ResponseEntity<? extends CommonResponse<StoreResponse>> getMyStore() {

        return ResponseEntity.ok(new CommonResponse<>(storeService.getMyStore()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends CommonResponse<StoreResponse>> getStore(
            @PathVariable("id") Long id
    ) {

        return ResponseEntity.ok(new CommonResponse<>(storeService.getStore(id)));
    }
}
