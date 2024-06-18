package com.portfolio.reservation.domain.reservation;

import com.portfolio.reservation.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
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
public class Reservation extends BaseEntity {

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    @JoinColumn(name = "reservation_id")
    private List<ReservationHistory> reservationHistories = new ArrayList<>();

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "time_table_id")
    private Long timeTableId;

    private ReservationStatus status;

    private LocalDateTime requestDateTime;

    private int persons;

    private LocalDate date;

    private LocalTime time;

    private String lastReason;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime expiredAt;

    public static Reservation create(
            Long userId,
            Long storeId,
            Long timeTableId,
            LocalDateTime requestDateTime,
            int persons,
            LocalDate date,
            LocalTime time,
            String lastReason
    ) {

        return Reservation.builder()
                .userId(userId)
                .storeId(storeId)
                .timeTableId(timeTableId)
                .requestDateTime(requestDateTime)
                .persons(persons)
                .date(date)
                .time(time)
                .lastReason(lastReason)
                .status(ReservationStatus.RESERVE_CONFIRM)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
