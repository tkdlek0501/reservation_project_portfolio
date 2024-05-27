package com.portfolio.reservation.controller.user;

import com.portfolio.reservation.common.CommonResponse;
import com.portfolio.reservation.dto.user.UserCreateRequest;
import com.portfolio.reservation.dto.user.UserPasswordUpdateRequest;
import com.portfolio.reservation.dto.user.UserResponse;
import com.portfolio.reservation.dto.user.UserUpdateRequest;
import com.portfolio.reservation.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Api", description = "회원 관련 API")
@Slf4j
@RestController
@RequestMapping("/v1.0/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<? extends CommonResponse> signUp(
            @Valid @RequestBody UserCreateRequest request
    ) {

        userService.signUp(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @PatchMapping("")
    public ResponseEntity<? extends CommonResponse> update(
            @Valid @RequestBody UserUpdateRequest request
    ) {

        userService.update(request);
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @PatchMapping("/password")
    public ResponseEntity<? extends CommonResponse> updatePassword(
            @Valid @RequestBody UserPasswordUpdateRequest request
    ) {

        userService.updatePassword(request.getCheckPassword(), request.getUpdatePassword());
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @DeleteMapping("")
    public ResponseEntity<? extends CommonResponse> delete(
            @Valid @RequestBody UserPasswordUpdateRequest request
    ) {

        userService.delete(request.getCheckPassword());
        return ResponseEntity.ok(new CommonResponse<>(""));
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends CommonResponse<UserResponse>> getUser(
            @PathVariable("id") Long id
    ) {

        return ResponseEntity.ok(new CommonResponse<>(userService.getUser(id)));
    }

    @GetMapping("")
    public ResponseEntity<? extends CommonResponse<UserResponse>> getMe() throws Exception {

        return ResponseEntity.ok(new CommonResponse<>(userService.getMe()));
    }
}
