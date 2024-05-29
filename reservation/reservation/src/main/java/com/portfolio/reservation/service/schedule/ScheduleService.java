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

    public Schedule findByStoreId(Long storeId) {
        return scheduleRepository.findByStoreIdAndExpiredAtIsNull(storeId)
                .orElseThrow(NotFoundScheduleException::new);
    }

    // TODO: 기본 운영 방식 삭제

    // TODO: 예약 스케줄 주간 조회

    // TODO: 예약 가능 인원 수정

    // TODO: 당일 예약 관련 설정 수정

    // TODO: 해당일의 타임 테이블 조회 -> timeTableService

    // TODO: 해당 dateTable 및 timeTable 수정 -> dateTableService

    // TODO: 예약 가능 일자 조회 -> dateTableService

    // TODO: 예약 가능 시간 조회 -> timeTableService
}
