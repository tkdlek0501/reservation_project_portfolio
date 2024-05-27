package com.portfolio.reservation.dto.store;

import com.portfolio.reservation.domain.store.Store;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreCreateRequest {

    @NotNull(message = "매장명은 필수값입니다.")
    @Size(max = 16, message = "매장명은 최대 16자여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "매장명은 한글, 영문자, 숫자만 포함할 수 있습니다.")
    private String name;

    public static Store toEntity(StoreCreateRequest request, Long userId) {

        return Store.builder()
                .userId(userId)
                .name(request.getName())
                .build();
    }
}
