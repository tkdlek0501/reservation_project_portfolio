package com.portfolio.reservation.advice;

import com.portfolio.reservation.common.CommonResponse;
import com.portfolio.reservation.common.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 예상치 못한 exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<? extends CommonResponse> handleException(Exception e) {

        log.error("예상치 못한 에러 발생 : ", e);
        return ResponseEntity.internalServerError().body(new CommonResponse<>(e));
    }

    // 커스텀 exception
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<? extends CommonResponse> handleCustomException(CustomException e) {

        return ResponseEntity.ok(new CommonResponse(e));
    }

    // validation exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<? extends CommonResponse> handleValidationException(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();

        Map<String, String> errors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (e1, e2) -> e1 + ", " + e2));

        return ResponseEntity.badRequest().body(new CommonResponse(errors, "유효성 검증에 실패했습니다."));
    }
}
