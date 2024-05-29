package com.portfolio.reservation.domain.schedule;

import com.portfolio.reservation.domain.common.BaseEntity;
import com.portfolio.reservation.domain.schedule.type.TimeUnitType;
import com.portfolio.reservation.domain.timetable.DateTable;
import com.portfolio.reservation.domain.timetable.TimeTable;
import com.portfolio.reservation.dto.schedule.DateOperationRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.apache.ibatis.annotations.One;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class DateOperation extends BaseEntity {

    private Long scheduleId;

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    private List<TimeOperation> timeOperations = new ArrayList<>();

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    private List<DateTable> dateTables = new ArrayList<>();

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    private List<TimeTable> timeTables = new ArrayList<>();

    private TimeUnitType timeUnit;

    private LocalDate startDate;

    private LocalDate endDate;

    private int maxPerson;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime expiredAt;

    public static DateOperation create(
            Long scheduleId,
            DateOperationRequest request
    ) {

        return DateOperation.builder()
                .scheduleId(scheduleId)
                .timeUnit(request.getTimeUnit())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .maxPerson(request.getMaxPerson())
                .build();
    }

    public void update(LocalDate startDate, LocalDate endDate) {

        this.startDate = startDate;
        this.endDate = endDate;
    }
}
