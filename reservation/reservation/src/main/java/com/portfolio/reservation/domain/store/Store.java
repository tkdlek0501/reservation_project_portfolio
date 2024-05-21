package com.portfolio.reservation.domain.store;

import com.portfolio.reservation.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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

    private Long userId;
    // user 와 1:1 관계
    // OneToOne 이므로 N+1 관리 측면에서 연관관계 설정 별도 x

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
