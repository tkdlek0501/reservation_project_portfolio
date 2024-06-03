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
import java.util.Objects;

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

    private boolean available; // 해당 일자 예약 사용 여부

    private boolean isHourlySetting; // 인원을 timeTable 설정대로 적용할지 여부

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
                .available(true)
                .isHourlySetting(false)
                .build();
    }

    public void updateDailyAvailable(boolean available) {

        this.available = available;
    }

    public void update(boolean available, int maxPersonOfDay, Boolean isHourlySetting) {

        this.available = available;
        this.maxPerson = maxPersonOfDay;
        this.isHourlySetting = !Objects.isNull(isHourlySetting) && isHourlySetting;
    }
}
