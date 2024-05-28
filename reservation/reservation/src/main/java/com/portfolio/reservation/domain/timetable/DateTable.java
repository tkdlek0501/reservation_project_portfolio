package com.portfolio.reservation.domain.timetable;

import com.portfolio.reservation.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.*;
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
public class DateTable extends BaseEntity {

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    private List<TimeTable> timeTables = new ArrayList<>();

    private Long scheduleId;

    private Long dateOperationId;

    private Long timeOperationId;

    private Long storeId;

    private LocalDate date;

    private int maxPerson; // 해당 데이트테이블 최대 인원수

    private boolean dailyAvailable; // 해당 일자 예약 사용 여부

    private boolean isHourlySetting; // 인원을 시간대별로 설정할 지 여부

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime expiredAt;

    public static DateTable create(
            Long storeId,
            Long scheduleId,
            Long dateOperationId,
            Long timeOperationId,
            LocalDate date,
            int maxPerson
    ) {
        return DateTable.builder()
                .storeId(storeId)
                .scheduleId(scheduleId)
                .dateOperationId(dateOperationId)
                .timeOperationId(timeOperationId)
                .date(date)
                .maxPerson(maxPerson)
                .dailyAvailable(true)
                .isHourlySetting(false)
                .build();
    }
}
