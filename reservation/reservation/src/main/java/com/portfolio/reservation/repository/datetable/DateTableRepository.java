package com.portfolio.reservation.repository.datetable;

import com.portfolio.reservation.domain.timetable.DateTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DateTableRepository extends JpaRepository<DateTable, Long> {
}
