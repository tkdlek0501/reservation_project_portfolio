package com.portfolio.reservation.repository.dateoperation;

import com.portfolio.reservation.domain.schedule.DateOperation;
import com.portfolio.reservation.domain.schedule.QDateOperation;
import com.portfolio.reservation.domain.schedule.QSchedule;
import com.portfolio.reservation.domain.schedule.Schedule;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DateOperationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QSchedule schedule = QSchedule.schedule;
    QDateOperation dateOperation = QDateOperation.dateOperation;

    public List<DateOperation> getAllUse(Schedule schedule) {

        return queryFactory
                .select(dateOperation)
                .from(dateOperation)
                .where(
                        scheduleEq(schedule),
                        expiredAtIsNull(),
                        futureDates()
                )
                .fetch();
    }

    private BooleanExpression scheduleEq(Schedule schedule) {
        return dateOperation.scheduleId.eq(schedule.getId());
    }

    private BooleanExpression expiredAtIsNull() {
        return dateOperation.expiredAt.isNull();
    }

    private BooleanExpression futureDates() {

        return dateOperation.endDate.after(LocalDate.now()).or(dateOperation.endDate.eq(LocalDate.now()));
//        BooleanExpression onCondition = dateOperation.isEndless.eq(false)
//                .and(dateOperation.endDate.after(LocalDate.now()).or(dateOperation.endDate.eq(LocalDate.now())));

//        BooleanExpression offCondition = dateOperation.isEndless.eq(true);
//
//        return onCondition.or(offCondition);
    }
}
