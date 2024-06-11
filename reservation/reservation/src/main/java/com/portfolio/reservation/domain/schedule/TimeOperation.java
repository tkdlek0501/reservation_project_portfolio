package com.portfolio.reservation.domain.schedule;

import com.portfolio.reservation.domain.common.BaseEntity;
import com.portfolio.reservation.domain.timetable.TimeTable;
import com.portfolio.reservation.dto.operation.TimeOperationUpdateRequest;
import com.portfolio.reservation.dto.schedule.TimeOperationRequest;
import jakarta.persistence.*;
import lombok.*;
import org.apache.ibatis.annotations.One;
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
public class TimeOperation extends BaseEntity {

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    @JoinColumn(name = "time_operation_id")
    private List<TimeTable> timeTables = new ArrayList<>();

    @Column(name = "date_operation_id", nullable = false)
    private Long dateOperationId;

    private LocalTime startTime;

    private LocalTime endTime;

    private int maxPerson;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime expiredAt;

    public static TimeOperation create(
            Long dateOperationId,
            TimeOperationRequest request
    ) {

        return TimeOperation.builder()
                .dateOperationId(dateOperationId)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .maxPerson(request.getMaxPerson())
                .build();
    }

    public static TimeOperation create(
            Long dateOperationId,
            TimeOperationUpdateRequest request
    ) {

        return TimeOperation.builder()
                .dateOperationId(dateOperationId)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .maxPerson(request.getMaxPerson())
                .build();
    }
}
