package com.portfolio.reservation.domain.store;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStore is a Querydsl query type for Store
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStore extends EntityPathBase<Store> {

    private static final long serialVersionUID = -474860577L;

    public static final QStore store = new QStore("store");

    public final com.portfolio.reservation.domain.common.QBaseEntity _super = new com.portfolio.reservation.domain.common.QBaseEntity(this);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final ListPath<com.portfolio.reservation.domain.timetable.DateTable, com.portfolio.reservation.domain.timetable.QDateTable> dateTables = this.<com.portfolio.reservation.domain.timetable.DateTable, com.portfolio.reservation.domain.timetable.QDateTable>createList("dateTables", com.portfolio.reservation.domain.timetable.DateTable.class, com.portfolio.reservation.domain.timetable.QDateTable.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> expiredAt = createDateTime("expiredAt", java.time.LocalDateTime.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final ListPath<com.portfolio.reservation.domain.reservation.Reservation, com.portfolio.reservation.domain.reservation.QReservation> reservations = this.<com.portfolio.reservation.domain.reservation.Reservation, com.portfolio.reservation.domain.reservation.QReservation>createList("reservations", com.portfolio.reservation.domain.reservation.Reservation.class, com.portfolio.reservation.domain.reservation.QReservation.class, PathInits.DIRECT2);

    public final ListPath<com.portfolio.reservation.domain.schedule.Schedule, com.portfolio.reservation.domain.schedule.QSchedule> schedules = this.<com.portfolio.reservation.domain.schedule.Schedule, com.portfolio.reservation.domain.schedule.QSchedule>createList("schedules", com.portfolio.reservation.domain.schedule.Schedule.class, com.portfolio.reservation.domain.schedule.QSchedule.class, PathInits.DIRECT2);

    public final ListPath<com.portfolio.reservation.domain.timetable.TimeTable, com.portfolio.reservation.domain.timetable.QTimeTable> timeTables = this.<com.portfolio.reservation.domain.timetable.TimeTable, com.portfolio.reservation.domain.timetable.QTimeTable>createList("timeTables", com.portfolio.reservation.domain.timetable.TimeTable.class, com.portfolio.reservation.domain.timetable.QTimeTable.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QStore(String variable) {
        super(Store.class, forVariable(variable));
    }

    public QStore(Path<? extends Store> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStore(PathMetadata metadata) {
        super(Store.class, metadata);
    }

}

