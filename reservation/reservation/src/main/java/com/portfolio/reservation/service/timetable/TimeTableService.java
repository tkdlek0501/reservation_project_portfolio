package com.portfolio.reservation.service.timetable;

import com.portfolio.reservation.domain.timetable.TimeTable;
import com.portfolio.reservation.repository.timetable.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;

    public List<TimeTable> save(List<TimeTable> timeTableList) {
        return timeTableRepository.saveAll(timeTableList);
    }
}
