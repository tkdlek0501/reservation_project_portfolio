package com.portfolio.reservation.domain.timetable;

import com.portfolio.reservation.domain.common.BaseEntity;
import com.portfolio.reservation.domain.reservation.Reservation;
import com.portfolio.reservation.domain.schedule.type.TimeUnitType;
import com.portfolio.reservation.dto.operation.TimeTableRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class TimeTable extends BaseEntity {

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    @JoinColumn(name = "time_table_id")
    private List<Reservation> reservations = new ArrayList<>();

    @Column(name = "date_table_id")
    private Long dateTableId;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "date_operation_id")
    private Long dateOperationId;

    @Column(name = "time_operation_id")
    private Long timeOperationId;

    private LocalDate date;

    private LocalTime time;

    private int maxPerson;

    private boolean available; // 해당 시간 활성 여부

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime expiredAt;

    public static TimeTable create(
            Long storeId,
            Long scheduleId,
            Long dateTableId,
            Long dateOperationId,
            Long timeOperationId,
            LocalDate date,
            LocalTime time,
            int maxPerson
    ) {

        return TimeTable.builder()
                .storeId(storeId)
                .scheduleId(scheduleId)
                .dateTableId(dateTableId)
                .dateOperationId(dateOperationId)
                .timeOperationId(timeOperationId)
                .date(date)
                .time(time)
                .maxPerson(maxPerson)
                .available(true)
                .build();
    }

    public void update(TimeTableRequest request) {

        this.available = request.isAvailable();
        this.maxPerson = request.getMaxPersonOfTime();
    }

    public static LocalTime makeStartDateOfDateTable(List<TimeTable> timeTables) {

        return timeTables
                .stream()
                .filter(tt -> Objects.isNull(tt.getExpiredAt()))
                .min(Comparator.comparing(TimeTable::getTime))
                .map(TimeTable::getTime)
                .orElse(null);
    }

    // 하나의 dateTable 내 timeTable 리스트를 통해 운영 종료 시간 추출
    public static LocalTime makeEndDateOfDateTable(
            List<TimeTable> timeTables, TimeUnitType timeUnit) {

        return timeTables
                .stream()
                .filter(tt -> Objects.isNull(tt.getExpiredAt()))
                .max(Comparator.comparing(TimeTable::getTime))
                .map(tt -> switch (timeUnit) {
                    case HALF -> tt.getTime().plusMinutes(30);
                    case HOUR -> tt.getTime().plusHours(1);
                })
                .orElse(null);
    }
}
