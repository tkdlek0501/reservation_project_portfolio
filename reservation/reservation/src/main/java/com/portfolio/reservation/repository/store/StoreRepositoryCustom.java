package com.portfolio.reservation.repository.store;

import com.portfolio.reservation.domain.store.QStore;
import com.portfolio.reservation.domain.user.QUser;
import com.portfolio.reservation.dto.store.StoreWithUserDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QUser user = QUser.user;

    QStore store = QStore.store;

    public StoreWithUserDto findStoreWithUser(Long storeId) {
        return queryFactory
                .select(Projections.constructor(StoreWithUserDto.class,
                        store.id,
                        store.name,
                        user.nickname
                        ))
                .from(store)
                .leftJoin(user)
                .on(store.userId.eq(user.id))
                .where(
                        store.expiredAt.isNull(),
                        user.expiredAt.isNull(),
                        store.id.eq(storeId)
                )
                .fetchOne();
    }
}
