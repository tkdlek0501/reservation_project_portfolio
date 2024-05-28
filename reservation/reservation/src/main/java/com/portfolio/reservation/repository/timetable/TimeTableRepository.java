package com.portfolio.reservation.repository.timetable;

import com.portfolio.reservation.domain.timetable.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
}
