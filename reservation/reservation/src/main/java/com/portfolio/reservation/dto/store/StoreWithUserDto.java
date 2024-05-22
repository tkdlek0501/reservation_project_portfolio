package com.portfolio.reservation.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreWithUserDto {

    private Long storeId;

    private String storeName;

    private String nickName;
}
