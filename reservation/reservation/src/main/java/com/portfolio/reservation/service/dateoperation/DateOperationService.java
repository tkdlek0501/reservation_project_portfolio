package com.portfolio.reservation.service.dateoperation;

import com.portfolio.reservation.domain.schedule.DateOperation;
import com.portfolio.reservation.domain.schedule.Schedule;
import com.portfolio.reservation.dto.operation.DateOperationUpdateRequest;
import com.portfolio.reservation.dto.operation.DateOperationUpdateRequests;
import com.portfolio.reservation.dto.operation.TimeOperationUpdateRequest;
import com.portfolio.reservation.dto.schedule.DateOperationRequest;
import com.portfolio.reservation.exception.dateoperation.NotFoundDateOperationException;
import com.portfolio.reservation.exception.operation.ExceedDateOperationSizeException;
import com.portfolio.reservation.exception.operation.OverlapDateOperationException;
import com.portfolio.reservation.exception.operation.ExceedDateOperationDateException;
import com.portfolio.reservation.exception.operation.OverlapDatesOperationException;
import com.portfolio.reservation.exception.operation.OverlapTimeOperationException;
import com.portfolio.reservation.repository.dateoperation.DateOperationRepository;
import com.portfolio.reservation.repository.dateoperation.DateOperationRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DateOperationService {

    private final DateOperationRepository dateOperationRepository;
    private final DateOperationRepositoryCustom dateOperationRepositoryCustom;

    public void checkAlreadyExist(DateOperationRequest request, Schedule schedule) {

        long daysBetween = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (daysBetween > 30) {
            throw new ExceedDateOperationDateException();
        }

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

    public List<DateOperation> findByScheduleId(Long scheduleId) {

        return dateOperationRepository.getByNow(scheduleId, LocalDate.now());
    }

    public void validateDateOperation(DateOperationUpdateRequests request) {

        for (DateOperationUpdateRequest dateOperation : request.getDateOperations()) {
            long daysBetween = ChronoUnit.DAYS.between(dateOperation.getStartDate(), dateOperation.getEndDate());
            if (daysBetween > 30) {
                throw new ExceedDateOperationDateException();
            }
        }

        List<LocalDate> startDates = request.getDateOperations()
                .stream()
                .map(DateOperationUpdateRequest::getStartDate)
                .collect(Collectors.toList());

        List<LocalDate> endDates = request.getDateOperations()
                .stream()
                .map(DateOperationUpdateRequest::getEndDate)
                .collect(Collectors.toList());

        // false 인게 하나라도 있는지 확인 (anyMatch true 있으면 noneMatch false)
        boolean isValid = IntStream.range(0, startDates.size())
                .noneMatch(i -> IntStream.range(0, startDates.size())
                        .anyMatch(j -> i != j && !isValidScheduleOperation(i, j, startDates, endDates)));

        if (!isValid) {
            throw new OverlapDatesOperationException();
        }
    }

    private boolean isValidScheduleOperation(int i, int j, List<LocalDate> startDates, List<LocalDate> endDates) {
        LocalDate iEndDate = endDates.get(i);
        LocalDate jEndDate = endDates.get(j);

        return (startDates.get(i).isBefore(startDates.get(j)) && iEndDate.isBefore(startDates.get(j)))
                || (startDates.get(i).isAfter(jEndDate) && iEndDate.isAfter(jEndDate));
    }

    public void validateTimeOperation(List<DateOperationUpdateRequest> requests) {

        requests.forEach(req -> {
            List<TimeOperationUpdateRequest> times = req.getTimeOperations();

            IntStream.range(0, times.size())
                    .flatMap(i -> IntStream.range(0, times.size())
                            .filter(j -> i != j && !validateTime(times.get(i).getStartTime(), times.get(i).getEndTime(), times.get(j).getStartTime(), times.get(j).getEndTime()))
                    )
                    .findFirst() // validation 통과못하는 것을 찾아서
                    .ifPresent(index -> { // 존재하면
                        throw new OverlapTimeOperationException();
                    });
        });
    }

    private static boolean validateTime(LocalTime startTime, LocalTime endTime, LocalTime otherStartTime, LocalTime otherEndTime) {

        return (startTime.isBefore(otherStartTime) && (endTime.isBefore(otherStartTime) || endTime.equals(otherStartTime)))
                || ((startTime.isAfter(otherEndTime) || startTime.equals(otherEndTime)) && endTime.isAfter(otherEndTime));
    }

    public DateOperation findById(Long id) {

        return dateOperationRepository.findByIdAndExpiredAtIsNull(id)
                .orElseThrow(NotFoundDateOperationException::new);
    }
}
