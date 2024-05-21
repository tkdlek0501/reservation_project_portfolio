package com.portfolio.reservation.dto.store;

import com.portfolio.reservation.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreCreateRequest {

    private String name;

    public static Store toEntity(StoreCreateRequest request, Long userId) {

        return Store.builder()
                .userId(userId)
                .name(request.getName())
                .build();
    }
}
