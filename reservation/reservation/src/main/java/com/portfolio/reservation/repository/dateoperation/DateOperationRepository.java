package com.portfolio.reservation.repository.dateoperation;

import com.portfolio.reservation.domain.schedule.DateOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DateOperationRepository extends JpaRepository<DateOperation, Long> {
}
