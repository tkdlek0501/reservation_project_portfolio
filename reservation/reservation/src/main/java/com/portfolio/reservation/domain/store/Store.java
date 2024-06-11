package com.portfolio.reservation.domain.store;

import com.portfolio.reservation.domain.common.BaseEntity;
import com.portfolio.reservation.domain.reservation.Reservation;
import com.portfolio.reservation.domain.schedule.Schedule;
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
@Table(name = "store")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Store extends BaseEntity {

    @Column(length = 50, unique = true)
    private String name; // 매장명

    @Column(name = "user_id")
    private Long userId;
    // user 와 1:1 관계
    // OneToOne 이므로 N+1 관리 측면에서 연관관계 설정 별도 x

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    private List<Schedule> schedules = new ArrayList<>();

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    private List<DateTable> dateTables = new ArrayList<>();

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    private List<TimeTable> timeTables = new ArrayList<>();

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    private List<Reservation> reservations = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime expiredAt;

    public void updateName(String name) {
        this.name = name;
    }

    public void expire() {
        this.expiredAt = LocalDateTime.now();
    }
}
