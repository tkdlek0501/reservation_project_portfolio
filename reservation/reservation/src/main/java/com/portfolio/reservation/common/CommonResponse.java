package com.portfolio.reservation.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "공통 response")
public class CommonResponse<T> {

    @Schema(description = "결과 코드")
    private String code;

    @Schema(description = "에러 메세지")
    private String message;

    @Schema(description = "결과 상태 (1 : 성공, -1 : 실패)")
    private String status;

    @Schema(description = "조회 결과")
    private T data;

    // 성공시
    public CommonResponse(T data) {
        this.code = "OK";
        this.status = "1";
        this.data = data;
    }

    // 커스텀 에러시
    public CommonResponse(CustomException e) {
        this.code = e.getCode();
        this.status = "-1";
        this.message = e.getMessage();
    }

    // 예상 못한 에러시
    public CommonResponse(Exception e) {
        this.code = "-1";
        this.status = "-1";
        this.message = e.getMessage();
    }

    // validation 에러시
    public CommonResponse(T error, String msg) {
        this.code = "-1";
        this.status = "-1";
        this.data = error;
        this.message = msg;
    }
}
