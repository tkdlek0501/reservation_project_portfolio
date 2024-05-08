package com.portfolio.reservation.controller;

import com.portfolio.reservation.dto.user.UserCreateRequest;
import com.portfolio.reservation.dto.user.UserPasswordUpdateRequest;
import com.portfolio.reservation.dto.user.UserResponse;
import com.portfolio.reservation.dto.user.UserUpdateRequest;
import com.portfolio.reservation.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Api", description = "회원 관련 API")
@Slf4j
@RestController
@RequestMapping("/v1.0/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(
            @RequestBody UserCreateRequest request
    ) {

        try {
            userService.signUp(request);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.info("예상치 못한 에러 발생 : {}, {}", e.getStackTrace(), e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    @PatchMapping("")
    public ResponseEntity<Void> update(
            @RequestBody UserUpdateRequest request
    ) {

        try {
            userService.update(request);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.info("예상치 못한 에러 발생 : {}, {}", e.getStackTrace(), e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @RequestBody UserPasswordUpdateRequest request
    ) {

        try {
            userService.updatePassword(request.getCheckPassword(), request.getUpdatePassword());
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.info("예상치 못한 에러 발생 : {}, {}", e.getStackTrace(), e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Void> delete(
            @RequestBody UserPasswordUpdateRequest request
    ) {

        try {
            userService.delete(request.getCheckPassword());
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.info("예상치 못한 에러 발생 : {}, {}", e.getStackTrace(), e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable("id") Long id
    ) {

        try {
            return ResponseEntity.ok(userService.getUser(id));
        } catch (Exception e) {
            log.info("예상치 못한 에러 발생 : {}, {}", e.getStackTrace(), e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    @GetMapping("")
    public ResponseEntity<UserResponse> getMe() {

        try {
            return ResponseEntity.ok(userService.getMe());
        } catch (Exception e) {
            log.info("예상치 못한 에러 발생 : {}, {}", e.getStackTrace(), e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    // TODO: Login jwt 발급 및 만료 등 서비스 작업
}
