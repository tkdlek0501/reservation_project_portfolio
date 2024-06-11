package com.portfolio.reservation.repository.dateoperation;

import com.portfolio.reservation.domain.schedule.DateOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DateOperationRepository extends JpaRepository<DateOperation, Long> {

    @Query("SELECT do FROM DateOperation do" +
            " WHERE do.expiredAt IS NULL" +
            " AND do.scheduleId = :scheduleId" +
            " AND do.endDate >= :now")
    List<DateOperation> getByNow(
            @Param("scheduleId") Long scheduleId,
            @Param("now") LocalDate now);

    Optional<DateOperation> findByIdAndExpiredAtIsNull(Long id);

    Optional<DateOperation> findTopByOrderById();
}
