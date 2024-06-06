package com.portfolio.reservation.repository.schedule;

import com.portfolio.reservation.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByStoreIdAndExpiredAtIsNull(Long storeId);

    Optional<Schedule> findByIdAndExpiredAtIsNull(Long id);
}
