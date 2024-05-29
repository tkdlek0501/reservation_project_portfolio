package com.portfolio.reservation.repository.timetable;

import com.portfolio.reservation.domain.timetable.TimeTable;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    @Modifying
    @Query("UPDATE TimeTable tt" +
            " SET tt.expiredAt = :now" +
            " WHERE tt.dateOperationId IN :dateOperationIds" +
            "   AND tt.expiredAt IS NULL")
    int bulkExpireByDateOperationIds(
            @Param("dateOperationIds") List<Long> dateOperationIds,
            @Param("now") LocalDateTime now
    );
}
