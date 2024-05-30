package com.portfolio.reservation.service.timetable;

import com.portfolio.reservation.domain.timetable.TimeTable;
import com.portfolio.reservation.dto.operation.TimeTableWithDateTableDto;
import com.portfolio.reservation.repository.timetable.TimeTableRepository;
import com.portfolio.reservation.repository.timetable.TimeTableRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;

    private final TimeTableRepositoryCustom timeTableRepositoryCustom;

    public List<TimeTable> save(List<TimeTable> timeTableList) {
        return timeTableRepository.saveAll(timeTableList);
    }

    public void bulkExpireByDateOperationIds(List<Long> dateOperationIds) {

        timeTableRepository.bulkExpireByDateOperationIds(dateOperationIds, LocalDateTime.now());
    }

    public List<TimeTableWithDateTableDto> search(Long storeId, LocalDate startDate, LocalDate endDate) {

        return timeTableRepositoryCustom.search(storeId, startDate, endDate);
    }
}
