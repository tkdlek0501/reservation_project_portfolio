package com.portfolio.reservation.domain.schedule;

import com.portfolio.reservation.domain.common.BaseEntity;
import com.portfolio.reservation.domain.timetable.TimeTable;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "timeOperation")
    @BatchSize(size = 500)
    private List<TimeTable> timeTables = new ArrayList<>();

    private Long dateOperationId;

    private LocalTime startTime;

    private LocalTime endTime;

    private int maxPerson;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
