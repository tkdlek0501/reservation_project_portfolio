package com.portfolio.reservation.domain.reservation;

import com.portfolio.reservation.domain.common.BaseEntity;
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
public class Reservation extends BaseEntity {

    @Builder.Default
    @OneToMany
    @BatchSize(size = 500)
    private List<ReservationHistory> reservationHistories = new ArrayList<>();

    private Long userId;

    private Long storeId;

    private Long timeTableId;

    private String status;

    private LocalDateTime requestDateTime;

    private int person;

    private String lastReason;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
