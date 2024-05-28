package com.portfolio.reservation.domain.timetable;

import com.portfolio.reservation.domain.common.BaseEntity;
import com.portfolio.reservation.domain.reservation.Reservation;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<Reservation> reservations = new ArrayList<>();

    private Long dateTableId;

    private Long storeId;

    private Long scheduleId;

    private Long dateOperationId;

    private Long timeOperationId;

    private LocalTime time;

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
            LocalTime time
    ) {

        return TimeTable.builder()
                .storeId(storeId)
                .scheduleId(scheduleId)
                .dateTableId(dateTableId)
                .dateOperationId(dateOperationId)
                .timeOperationId(timeOperationId)
                .time(time)
                .available(true)
                .build();
    }
}
