package com.portfolio.reservation.controller;

import com.portfolio.reservation.dto.user.UserCreateRequest;
import com.portfolio.reservation.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Api", description = "회원 관련 API")
@Slf4j
@RestController
@RequestMapping("/v1.0/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody UserCreateRequest request) {

        try {
            userService.signUp(request);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.info("예상치 못한 에러 발생 : {}, {}", e.getStackTrace(), e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }
}
