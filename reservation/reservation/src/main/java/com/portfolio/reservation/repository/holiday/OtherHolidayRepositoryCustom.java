package com.portfolio.reservation.repository.holiday;

import com.portfolio.reservation.domain.schedule.*;
import com.portfolio.reservation.domain.store.QStore;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OtherHolidayRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QSchedule schedule = QSchedule.schedule;
    QOtherHoliday otherHoliday = QOtherHoliday.otherHoliday;

    public List<OtherHoliday> searchByStoreId(Long storeId, LocalDate startDate, LocalDate endDate) {

        return queryFactory
                .select(otherHoliday)
                .from(otherHoliday)
                .leftJoin(schedule)
                .on(schedule.id.eq(otherHoliday.scheduleId))
                .where(
                        schedule.storeId.eq(storeId),
                        otherHoliday.expiredAt.isNull(),
                        otherHoliday.startDate.goe(startDate),
                        otherHoliday.endDate.loe(endDate)
                )
                .fetch();
    }
}
