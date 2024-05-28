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

    private Long storeId;

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    private List<DateOperation> dateOperations = new ArrayList<>();

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    private List<RegularHoliday> regularHolidays = new ArrayList<>();

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    private List<OtherHoliday> otherHolidays = new ArrayList<>();

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    private List<DateTable> dateTables = new ArrayList<>();

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    private List<TimeTable> timeTables = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SameDayApprovalType sameDayRequestApproval; // 당일 예약 가능

    @Enumerated(EnumType.STRING)
    private SameDayApprovalType sameDayCancelApproval; // 당일 취소 가능

    private boolean useHoliday; // 휴무일 사용 여부

    private int requestMaxPerson; // 한 번에 예약 가능한 최대 인원 수

    private int requestMinPerson; // 한 번에 예약 가능한 최소 인원 수

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime expiredAt;

    public static Schedule create(
            Long storeId,
            SameDayApprovalType sameDayRequestApproval,
            SameDayApprovalType sameDayCancelApproval,
            boolean useHoliday,
            int requestMaxPerson,
            int requestMinPerson
    ) {

        return Schedule.builder()
                .storeId(storeId)
                .sameDayRequestApproval(sameDayRequestApproval)
                .sameDayCancelApproval(sameDayCancelApproval)
                .useHoliday(useHoliday)
                .requestMaxPerson(requestMaxPerson)
                .requestMinPerson(requestMinPerson)
                .build();
    }

//    @PrePersist
//    public void prePersist() {
        // 엔터티가 저장되기 전에 실행되는 로직
//        if (data == null) {
//            data = "Default Data";
//        }
//    }
}
