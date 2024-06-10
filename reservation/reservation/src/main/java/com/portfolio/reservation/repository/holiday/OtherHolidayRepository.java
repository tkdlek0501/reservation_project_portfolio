package com.portfolio.reservation.repository.holiday;

import com.portfolio.reservation.domain.schedule.OtherHoliday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtherHolidayRepository extends JpaRepository<OtherHoliday, Long> {
}
