package com.portfolio.reservation.dto.operation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class ScheduleLimitCountRequest {

    @Schema(description = "최소인원", example = "1")
    @Min(1)
    private Integer requestMinPerson;

    @Schema(description = "최대인원", example = "4")
    @Max(1)
    private Integer requestMaxPerson;
}
