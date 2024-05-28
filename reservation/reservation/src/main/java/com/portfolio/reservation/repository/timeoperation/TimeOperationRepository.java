package com.portfolio.reservation.repository.timeoperation;

import com.portfolio.reservation.domain.schedule.TimeOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeOperationRepository extends JpaRepository<TimeOperation, Long> {
}
