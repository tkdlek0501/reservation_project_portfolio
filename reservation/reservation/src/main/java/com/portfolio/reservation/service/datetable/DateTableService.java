package com.portfolio.reservation.service.datetable;

import com.portfolio.reservation.domain.timetable.DateTable;
import com.portfolio.reservation.repository.datetable.DateTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DateTableService {

    private final DateTableRepository dateTableRepository;

    public DateTable save(DateTable dateTable) {
        return dateTableRepository.save(dateTable);
    }
}
