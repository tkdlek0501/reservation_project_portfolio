package com.portfolio.reservation.repository.holiday;

import com.portfolio.reservation.domain.schedule.RegularHoliday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegularHolidayRepository extends JpaRepository<RegularHoliday, Long> {
}
