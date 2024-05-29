package com.portfolio.reservation.service.datetable;

import com.portfolio.reservation.domain.timetable.DateTable;
import com.portfolio.reservation.repository.datetable.DateTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DateTableService {

    private final DateTableRepository dateTableRepository;

    public DateTable save(DateTable dateTable) {
        return dateTableRepository.save(dateTable);
    }

    public void bulkExpireByDateOperationIds(List<Long> dateOperationIds) {

        dateTableRepository.bulkExpireByDateOperationIds(dateOperationIds, LocalDateTime.now());
    }
}
