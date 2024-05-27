package com.portfolio.reservation.domain.schedule;

import com.portfolio.reservation.domain.common.BaseEntity;
import com.portfolio.reservation.domain.schedule.type.SameDayApprovalType;
import com.portfolio.reservation.domain.timetable.DateTable;
import com.portfolio.reservation.domain.timetable.TimeTable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Schedule extends BaseEntity {

    @OneToMany(mappedBy = "schedule")
    @BatchSize(size = 500)
    private List<DateOperation> dateOperations = new ArrayList<>();

    @OneToMany(mappedBy = "schedule")
    @BatchSize(size = 500)
    private List<RegularHoliday> regularHolidays = new ArrayList<>();

    @OneToMany(mappedBy = "schedule")
    @BatchSize(size = 500)
    private List<OtherHoliday> otherHolidays = new ArrayList<>();

    @OneToMany(mappedBy = "schedule")
    @BatchSize(size = 500)
    private List<DateTable> dateTables = new ArrayList<>();

    @OneToMany(mappedBy = "schedule")
    @BatchSize(size = 500)
    private List<TimeTable> timeTables = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SameDayApprovalType sameDayRequestApproval;

    @Enumerated(EnumType.STRING)
    private SameDayApprovalType sameDayCancelApproval;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
