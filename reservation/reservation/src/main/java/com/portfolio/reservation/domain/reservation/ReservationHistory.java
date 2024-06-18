package com.portfolio.reservation.domain.reservation;

import com.portfolio.reservation.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ReservationHistory extends BaseEntity {

    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    private Long userId;

    private Long timeTableId;

    private ReservationStatus status;

    private LocalDate date;

    private LocalTime time;

    private int persons;

    private String reserveRequest;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static ReservationHistory create(
            Long reservationId,
            Long userId,
            Long timeTableId,
            LocalDate date,
            LocalTime time,
            int persons,
            String reserveRequest
    ) {

        return ReservationHistory.builder()
                .reservationId(reservationId)
                .userId(userId)
                .timeTableId(timeTableId)
                .date(date)
                .time(time)
                .persons(persons)
                .reserveRequest(reserveRequest)
                .build();
    }
}
