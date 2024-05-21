package com.portfolio.reservation.dto.store;

import com.portfolio.reservation.domain.store.Store;
import com.portfolio.reservation.repository.store.StoreRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponse {

    private Long storeId;

    private String storeName;

    private String nickName;

    public static StoreResponse of(
            Store store,
            String nickName
    ) {

        return StoreResponse.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .nickName(nickName)
                .build();
    }
}
