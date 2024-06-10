package com.portfolio.reservation.domain.schedule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.reservation.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// 매주 휴무 요일
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class RegularHoliday extends BaseEntity {

    private Long scheduleId;

    private LocalDate startDate;

    @Column(columnDefinition = "json")
    private String dayOfWeek; // "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime expiredAt;

    public List<String> getDayOfWeekList() {
        ObjectMapper objectMapper = new ObjectMapper();
        if (StringUtils.hasText(this.dayOfWeek)) {
            try {
                List<String> dayOfWeeks = objectMapper.readValue(this.dayOfWeek, new TypeReference<>() {
                });

                return dayOfWeeks.stream()
                        .map(this::correctDayOfWeekString)
                        .collect(Collectors.toList());
            } catch (Exception e) {}
        }

        return null;
    }

    private String correctDayOfWeekString(String input) {
        switch(input.toUpperCase().trim()) {
            case "SUN": return "SUNDAY";
            case "MON": return "MONDAY";
            case "TUE": return "TUESDAY";
            case "WED": return "WEDNESDAY";
            case "THU": return "THURSDAY";
            case "FRI": return "FRIDAY";
            case "SAT": return "SATURDAY";
            default: return input;
        }
    }
}
