package com.portfolio.reservation.repository.timeoperation;

import com.portfolio.reservation.domain.schedule.TimeOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeOperationRepository extends JpaRepository<TimeOperation, Long> {

    @Modifying
    @Query("UPDATE TimeOperation to" +
            " SET to.expiredAt = :now" +
            " WHERE to.id IN :ids")
    int bulkExpire(
            @Param("ids") List<Long> ids,
           @Param("now") LocalDateTime now
    );

    Optional<TimeOperation> findByIdAndExpiredAtIsNull(Long id);
}
