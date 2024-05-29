package com.portfolio.reservation.repository.datetable;

import com.portfolio.reservation.domain.timetable.DateTable;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DateTableRepository extends JpaRepository<DateTable, Long> {

    @Modifying
    @Query("UPDATE DateTable dt" +
            " SET dt.expiredAt = :now" +
            " WHERE dt.dateOperationId IN :dateOperationIds" +
            "   AND dt.expiredAt IS NULL")
    int bulkExpireByDateOperationIds(
            @Param("dateOperationIds") List<Long> dateOperationIds,
            @Param("now")LocalDateTime now
    );
}
