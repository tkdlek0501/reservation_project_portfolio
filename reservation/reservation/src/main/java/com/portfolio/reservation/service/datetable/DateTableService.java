package com.portfolio.reservation.service.datetable;

import com.portfolio.reservation.domain.schedule.Schedule;
import com.portfolio.reservation.domain.timetable.DateTable;
import com.portfolio.reservation.exception.operation.NotFoundDateTableException;
import com.portfolio.reservation.repository.datetable.DateTableRepository;
import com.portfolio.reservation.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    public DateTable findById(Long id) {

        return dateTableRepository.findByIdAndExpiredAtIsNull(id)
                .orElseThrow(NotFoundDateTableException::new);
    }

    public List<DateTable> getOthersInDate(DateTable dateTable) {

        return dateTableRepository.getOthersInDate(dateTable.getScheduleId(), dateTable.getDate(), dateTable.getId());
    }

    public List<DateTable> findAllByDate(Long scheduleId, boolean dailyAvailable, LocalDate date) {

        return dateTableRepository.findAllByDate(scheduleId, dailyAvailable, date);

    }
}
