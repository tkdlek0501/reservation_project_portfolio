package com.portfolio.reservation.service.dateoperation;

import com.portfolio.reservation.domain.schedule.DateOperation;
import com.portfolio.reservation.domain.schedule.Schedule;
import com.portfolio.reservation.dto.schedule.DateOperationRequest;
import com.portfolio.reservation.exception.ExceedDateOperationSizeException;
import com.portfolio.reservation.exception.OverlapDateOperationException;
import com.portfolio.reservation.repository.dateoperation.DateOperationRepository;
import com.portfolio.reservation.repository.dateoperation.DateOperationRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DateOperationService {

    private final DateOperationRepository dateOperationRepository;

    private final DateOperationRepositoryCustom dateOperationRepositoryCustom;

    public void checkAlreadyExist(DateOperationRequest request, Schedule schedule) {

        List<DateOperation> orgDateOperations =  dateOperationRepositoryCustom.getAllUse(schedule);

        if (orgDateOperations.size() >= 3) {
            throw new ExceedDateOperationSizeException();
        }

        boolean isValid = orgDateOperations.stream()
                .allMatch(od -> { // 모두 true 면 true OR false
                    return (request.getStartDate().isBefore(od.getStartDate()) && request.getEndDate().isBefore(od.getStartDate()))
                            || (request.getStartDate().isAfter(od.getEndDate()) && request.getEndDate().isAfter(od.getEndDate()));
                });
        if (!isValid) {
            throw new OverlapDateOperationException();
        }
    }

    @Transactional
    public DateOperation save(DateOperation dateOperation) {
        return dateOperationRepository.save(dateOperation);
    }
}
