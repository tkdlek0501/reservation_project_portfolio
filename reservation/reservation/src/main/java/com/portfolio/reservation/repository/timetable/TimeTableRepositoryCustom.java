package com.portfolio.reservation.repository.timetable;

import com.portfolio.reservation.domain.schedule.QDateOperation;
import com.portfolio.reservation.domain.timetable.QDateTable;
import com.portfolio.reservation.domain.timetable.QTimeTable;
import com.portfolio.reservation.dto.operation.TimeTableWithDateTableDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TimeTableRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QTimeTable timeTable = QTimeTable.timeTable;

    QDateTable dateTable = QDateTable.dateTable;

    QDateOperation dateOperation = QDateOperation.dateOperation;

    public List<TimeTableWithDateTableDto> search(Long storeId, LocalDate startDate, LocalDate endDate) {

        return queryFactory
                .select(Projections.constructor(TimeTableWithDateTableDto.class,
                        dateTable.id,
                        dateOperation.timeUnit,
                        dateTable.date,
                        dateTable.maxPerson,
                        dateTable.available,
                        dateTable.isHourlySetting,
                        timeTable.id,
                        timeTable.time,
                        timeTable.maxPerson,
                        timeTable.available
                        ))
                .from(timeTable)
                .leftJoin(dateTable)
                .on(timeTable.dateTableId.eq(dateTable.id))
                .leftJoin(dateOperation)
                .on(dateTable.dateOperationId.eq(dateOperation.id))
                .where(
                        timeTable.storeId.eq(storeId),
                        timeTable.expiredAt.isNull(),
                        timeTable.date.between(startDate, endDate)
                )
                .fetch();
    }
}
