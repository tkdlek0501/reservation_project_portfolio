package com.portfolio.reservation.repository.holiday;

import com.portfolio.reservation.domain.schedule.QRegularHoliday;
import com.portfolio.reservation.domain.schedule.QSchedule;
import com.portfolio.reservation.domain.schedule.RegularHoliday;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RegularHolidayRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QRegularHoliday regularHoliday = QRegularHoliday.regularHoliday;
    QSchedule schedule = QSchedule.schedule;

    public RegularHoliday findByStoreId(Long storeId) {

        return queryFactory
                .select(regularHoliday)
                .from(regularHoliday)
                .leftJoin(schedule)
                .on(schedule.id.eq(regularHoliday.scheduleId))
                .where(
                        schedule.storeId.eq(storeId),
                        regularHoliday.expiredAt.isNull()
                )
                .fetchOne();
    }
}
