package com.portfolio.reservation.domain.schedule;

import com.portfolio.reservation.domain.common.BaseEntity;
import com.portfolio.reservation.domain.schedule.type.RegularHolidayUnitType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class RegularHoliday extends BaseEntity {

    private Long scheduleId;

    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    private RegularHolidayUnitType type;

    @Column(columnDefinition = "json")
    private String dayOfWeek;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
