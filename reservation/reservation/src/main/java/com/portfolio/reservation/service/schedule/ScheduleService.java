package com.portfolio.reservation.service.schedule;


import com.portfolio.reservation.domain.schedule.DateOperation;
import com.portfolio.reservation.domain.schedule.Schedule;
import com.portfolio.reservation.domain.schedule.TimeOperation;
import com.portfolio.reservation.domain.schedule.type.SameDayApprovalType;
import com.portfolio.reservation.domain.schedule.type.TimeUnitType;
import com.portfolio.reservation.domain.store.Store;
import com.portfolio.reservation.domain.timetable.DateTable;
import com.portfolio.reservation.domain.timetable.TimeTable;
import com.portfolio.reservation.domain.user.User;
import com.portfolio.reservation.dto.schedule.DateOperationRequest;
import com.portfolio.reservation.dto.schedule.TimeOperationRequest;
import com.portfolio.reservation.exception.schedule.NotFoundScheduleException;
import com.portfolio.reservation.repository.schedule.ScheduleRepository;
import com.portfolio.reservation.service.dateoperation.DateOperationService;
import com.portfolio.reservation.service.datetable.DateTableService;
import com.portfolio.reservation.service.store.StoreService;
import com.portfolio.reservation.service.timeoperation.TimeOperationService;
import com.portfolio.reservation.service.timetable.TimeTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final StoreService storeService;

    private final DateOperationService dateOperationService;

    private final TimeOperationService timeOperationService;

    private final DateTableService dateTableService;

    private final TimeTableService timeTableService;

    @Transactional
    public void create(
                   User user,
                   SameDayApprovalType sameDayRequestApproval,
                   SameDayApprovalType sameDayCancelApproval,
                   boolean useHoliday,
                   int requestMaxPerson,
                   int requestMinPerson) {

        Store store = storeService.findByUserId(user.getId());

        Schedule schedule = Schedule.create(
                store.getId(),
                sameDayRequestApproval,
                sameDayCancelApproval,
                useHoliday,
                requestMaxPerson,
                requestMinPerson
        );

        scheduleRepository.save(schedule);
    }

    // TODO: 기분 운영 방식 생성 -> dateOperation
    @Transactional
    public void createOperation(Long storeId, DateOperationRequest request) {

        // TODO: startDate 와 endDate 의 간격이 30일이 넘으면 안됨

        // schedule 조회
        Schedule schedule = scheduleRepository.findByStoreIdAndExpiredAtIsNull(storeId)
                .orElseThrow(NotFoundScheduleException::new);

        // 중복 validation
        dateOperationService.checkAlreadyExist(request, schedule);

        // DateOperation 생성
        DateOperation createdDateOperation = dateOperationService.save(DateOperation.create(schedule.getId(), request));

        // TimeOperation 생성
        List<TimeOperation> timeOperations = new ArrayList<>();
        for(TimeOperationRequest time : request.getTimeOperationRequests()) {
            timeOperations.add(TimeOperation.create(createdDateOperation.getId(), time));
        }
        List<TimeOperation> createdTimeOperations = timeOperationService.save(timeOperations);

        // date_table, time_table 생성
        // date_table 과 time_table 은 date_operation(날짜) 와 time_operation(시간) 을 바라보고 만들어진다
        // 해당 날짜 운영 방식에서 시작일 가져오기
        LocalDate startDate = createdDateOperation.getStartDate();
        LocalDate endDate = createdDateOperation.getEndDate();

        // 시작일 - 종료일로 모든 날짜/시간 데이터 생성
        saveTimeTableBetweenStartAndEnd(storeId, schedule, createdDateOperation, createdTimeOperations, endDate, startDate);
    }

    private List<TimeTable> saveTimeTableBetweenStartAndEnd(Long storeId, Schedule schedule, DateOperation dateOperation, List<TimeOperation> timeOperations, LocalDate finishDate, LocalDate currentDate) {

        List<TimeTable> timeTables = new ArrayList<>();
        while (!currentDate.isAfter(finishDate)) {

            for (TimeOperation timeOperation : timeOperations) {
                DateTable dateTable = dateTableService.save(DateTable.create(storeId, schedule.getId(), dateOperation.getId(), timeOperation.getId(), currentDate, timeOperation.getMaxPerson()));

                timeTables.addAll(createTimeTablesForADay(storeId, schedule.getId(), dateOperation, timeOperation, dateTable));
            }

            // 다음 날짜로 이동
            currentDate = currentDate.plusDays(1);
        }
        return timeTableService.save(timeTables);
    }

    private List<TimeTable> createTimeTablesForADay(Long storeId, Long scheduleId, DateOperation dateOperation, TimeOperation timeOperation, DateTable dateTable) {
        List<TimeTable> timeTablesForADay = new ArrayList<>();

        LocalTime currentTime = timeOperation.getStartTime();
        while (currentTime.isBefore(timeOperation.getEndTime())) {
            timeTablesForADay.add(TimeTable.create(storeId, scheduleId, dateTable.getId(), dateOperation.getId(), timeOperation.getId(), currentTime));
            currentTime = getCurrentTimeByTimeUnit(dateOperation, currentTime);
        }

        return timeTablesForADay;
    }

    private LocalTime getCurrentTimeByTimeUnit(DateOperation dateOperation, LocalTime currentTime) {

        if (Objects.requireNonNull(dateOperation.getTimeUnit()) == TimeUnitType.HALF) {
            return currentTime.plusMinutes(30);
        }
        return currentTime.plusHours(1); // HOUR
    }

    // TODO: 기본 운영 방식 조회

    // TODO: 기본 운영 방식 삭제

    // TODO: 예약 스케줄 주간 조회

    // TODO: 예약 가능 인원 수정

    // TODO: 당일 예약 관련 설정 수정

    // TODO: 해당일의 타임 테이블 조회 -> timeTableService

    // TODO: 해당 dateTable 및 timeTable 수정 -> dateTableService

    // TODO: 예약 가능 일자 조회 -> dateTableService

    // TODO: 예약 가능 시간 조회 -> timeTableService
}
