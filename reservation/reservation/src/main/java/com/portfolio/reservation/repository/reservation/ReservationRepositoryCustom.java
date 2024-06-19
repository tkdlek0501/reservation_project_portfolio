package com.portfolio.reservation.repository.reservation;

import com.portfolio.reservation.domain.reservation.QReservation;
import com.portfolio.reservation.domain.reservation.Reservation;
import com.portfolio.reservation.domain.reservation.ReservationStatus;
import com.portfolio.reservation.domain.schedule.QSchedule;
import com.portfolio.reservation.domain.store.QStore;
import com.portfolio.reservation.domain.timetable.DateTable;
import com.portfolio.reservation.domain.timetable.QDateTable;
import com.portfolio.reservation.domain.timetable.QTimeTable;
import com.portfolio.reservation.domain.user.QUser;
import com.portfolio.reservation.dto.reservation.*;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QReservation reservation = QReservation.reservation;
    QDateTable dateTable = QDateTable.dateTable;
    QStore store = QStore.store;
    QTimeTable timeTable = QTimeTable.timeTable;
    QSchedule schedule = QSchedule.schedule;
    QUser user = QUser.user;

    public Map<DateTable, Long> countByDateAndStatusesGroupByDateTable(Long scheduleId, List<LocalDate> dates, List<ReservationStatus> statuses, Long reservationId) {

        List<PersonDateDto> content = queryFactory
                .select(
                        Projections.constructor(PersonDateDto.class,
                                dateTable,
                                reservation.persons.sum())
                )
                .from(reservation)
                .leftJoin(timeTable)
                .on(timeTable.id.eq(reservation.timeTableId))
                .leftJoin(dateTable)
                .on(dateTable.id.eq(timeTable.dateTableId))
                .leftJoin(schedule)
                .on(schedule.id.eq(timeTable.scheduleId))
                .where(
//                        storeIdEq(storeId),
                        scheduleIdEq(scheduleId),
                        dateIn(dates),
                        statusIn(statuses),
                        reservationIdNotIn(reservationId),
                        reservation.expiredAt.isNull()
                )
                .groupBy(dateTable)
                .fetch();

        Map<DateTable, Long> result = new LinkedHashMap<>();
        for (PersonDateDto personDate : content) {
            result.put(personDate.getDateTable(), Long.valueOf(personDate.getPersons()));
        }

        return result;
    }

    /**
     * storeId가 같은지
     */
    private BooleanExpression storeIdEq(Long storeId) {
        return storeId != null ? store.id.eq(storeId) : null;
    }

    /**
     * scheduleId가 같은지
     */
    private BooleanExpression scheduleIdEq(Long scheduleId) {
        return scheduleId != null ? schedule.id.eq(scheduleId) : null;
    }

    /**
     * 해당 reservationId를 제외
     */
    private BooleanExpression reservationIdNotIn(Long reservationId) {
        return reservationId != null ? reservation.id.notIn(reservationId) : null;
    }

    /**
     * statuses에 포함되는지
     */
    private BooleanExpression statusIn(List<ReservationStatus> statuses) {
        return !statuses.isEmpty() ? reservation.status.in(statuses) : null;
    }

    /**
     * dates에 포함되는지
     */
    private BooleanExpression dateIn(List<LocalDate> dates) {
        return !dates.isEmpty() ? reservation.date.in(dates) : null;
    }

    public Map<LocalDateTime, Long> countByTimeAndStatusesGroupByTime(Long scheduleId, List<LocalTime> times, List<ReservationStatus> statuses, Long reservationId) {

        List<PersonDateTimeDto> content = queryFactory
                .select(
                        Projections.constructor(PersonDateTimeDto.class,
                                reservation.date,
                                reservation.time,
                                reservation.persons.sum())
                )
                .from(reservation)
                .where(
//                        storeIdEq(storeId),
                        scheduleIdEq(scheduleId),
                        timeIn(times),
                        statusIn(statuses),
                        reservationIdNotIn(reservationId),
                        reservation.expiredAt.isNull()
                )
                .groupBy(reservation.date, reservation.time)
                .fetch();

        Map<LocalDateTime, Long> result = new LinkedHashMap<>();
        for (PersonDateTimeDto personDateTime : content) {
            result.put(LocalDateTime.of(personDateTime.getDate(), personDateTime.getTime()), Long.valueOf(personDateTime.getPersons()));
        }

        return result;
    }

    /**
     * times에 포함되는지
     */
    private BooleanExpression timeIn(List<LocalTime> times) {
        return !times.isEmpty() ? reservation.time.in(times) : null;
    }

    /**
     * 검색 조건에 따라 예약을 조회한다.
     */
    public Page<ReservationDto> searchReservationByCondition(ReservationSearchCondition condition, Pageable pageable){

        List<ReservationDto> content =
                queryFactory
                        .select(Projections.constructor(ReservationDto.class,
                                reservation.status,
                                user.nickname,
                                reservation.persons,
                                reservation.requestDateTime,
                                reservation.id
                        ))
                        .from(reservation)
                        .leftJoin(user)
                        .on(user.id.eq(reservation.userId))
                        .where(
                                storeIdEq(condition.getStoreId()),
                                dateBetween(condition.getStartDate(), condition.getEndDate()),
                                statusIn(condition.getStatuses()),
                                keywordContainsByType(condition.getKeyword(), condition.getKeywordType()),
                                reservation.expiredAt.isNull()
                        )
                .orderBy()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(reservation.count())
                .from(reservation)
                .where(
                        storeIdEq(condition.getStoreId()),
                        dateBetween(condition.getStartDate(), condition.getEndDate()),
                        statusIn(condition.getStatuses()),
                        keywordContainsByType(condition.getKeyword(), condition.getKeywordType()),
                        reservation.expiredAt.isNull()
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, count != null ? count : 0);
    }

    public OrderSpecifier orderById() {

        PathBuilder orderByExpression = new PathBuilder(Reservation.class, "reservation");

        return new OrderSpecifier(Order.ASC, orderByExpression.get("reservationDate"));
    }

    private BooleanExpression dateBetween(LocalDate startDate, LocalDate endDate) {

        return startDate != null && endDate != null ? reservation.requestDateTime.between(LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX)) : null;
    }

    /**
     * ReservationKeywordType에 따라 조건
     */
    private BooleanExpression keywordContainsByType(String keyword, ReservationKeywordType type) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        if (type == ReservationKeywordType.NAME) {
            return keyword != null ? user.nickname.like('%' + keyword + '%') : null;
//        } else if (type == ReservationKeywordType.PHONE) {
        } else if (type == ReservationKeywordType.NO) {
            return keyword != null ? reservation.id.eq(Long.valueOf(keyword)) : null;
        }
        return null;
    }
}
