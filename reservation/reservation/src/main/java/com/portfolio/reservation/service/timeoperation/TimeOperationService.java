package com.portfolio.reservation.service.timeoperation;

import com.portfolio.reservation.domain.schedule.TimeOperation;
import com.portfolio.reservation.repository.timeoperation.TimeOperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimeOperationService {

    private final TimeOperationRepository timeOperationRepository;

    public List<TimeOperation> save(List<TimeOperation> timeOperationList) {
        return timeOperationRepository.saveAll(timeOperationList);
    }

    public void bulkExpire(List<Long> ids) {
        timeOperationRepository.bulkExpire(ids, LocalDateTime.now());
    }
}

