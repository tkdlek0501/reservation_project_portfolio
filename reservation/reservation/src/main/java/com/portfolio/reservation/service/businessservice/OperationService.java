package com.portfolio.reservation.service.businessservice;

import com.portfolio.reservation.domain.common.BaseEntity;
import com.portfolio.reservation.domain.reservation.Reservation;
import com.portfolio.reservation.domain.schedule.*;
import com.portfolio.reservation.domain.schedule.type.TimeUnitType;
import com.portfolio.reservation.domain.timetable.DateTable;
import com.portfolio.reservation.domain.timetable.TimeTable;
import com.portfolio.reservation.dto.operation.*;
import com.portfolio.reservation.dto.schedule.DateOperationRequest;
import com.portfolio.reservation.dto.schedule.TimeOperationRequest;
import com.portfolio.reservation.exception.operation.NotAllowedTimeException;
import com.portfolio.reservation.exception.operation.NotSearchTimeTableException;
import com.portfolio.reservation.exception.operation.OverlapTimeOperationException;
import com.portfolio.reservation.repository.timetable.TimeTableRepository;
import com.portfolio.reservation.service.dateoperation.DateOperationService;
import com.portfolio.reservation.service.datetable.DateTableService;
import com.portfolio.reservation.service.holiday.OtherHolidayService;
import com.portfolio.reservation.service.holiday.RegularHolidayService;
import com.portfolio.reservation.service.schedule.ScheduleService;
import com.portfolio.reservation.service.timeoperation.TimeOperationService;
import com.portfolio.reservation.service.timetable.TimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.portfolio.reservation.domain.timetable.TimeTable.makeEndDateOfDateTable;
import static com.portfolio.reservation.domain.timetable.TimeTable.makeStartDateOfDateTable;
import static com.portfolio.reservation.service.dateoperation.DateOperationService.validateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class OperationService {

    private final ScheduleService scheduleService;
    private final DateOperationService dateOperationService;
    private final TimeOperationService timeOperationService;
    private final DateTableService dateTableService;
    private final TimeTableService timeTableService;
    private final ReservationService reservationService;
    private final OtherHolidayService otherHolidayService;
    private final RegularHolidayService regularHolidayService;

    /**
     * 기본 운영 시간을 생성합니다.
     */
    @Transactional
    public void createOperation(Long storeId, DateOperationRequest request) {

        // schedule 조회
        Schedule schedule = scheduleService.findByStoreId(storeId);

        // validation
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
        saveTimeTableBetweenStartAndEnd(storeId, schedule.getId(), createdDateOperation, createdTimeOperations, endDate, startDate);
    }

    private List<TimeTable> saveTimeTableBetweenStartAndEnd(Long storeId, Long scheduleId, DateOperation dateOperation, List<TimeOperation> timeOperations, LocalDate finishDate, LocalDate currentDate) {

        List<TimeTable> timeTables = new ArrayList<>();
        while (!currentDate.isAfter(finishDate)) {

            for (TimeOperation timeOperation : timeOperations) {
                DateTable dateTable = dateTableService.save(DateTable.create(storeId, scheduleId, dateOperation.getId(), timeOperation.getId(), currentDate, timeOperation.getMaxPerson()));

                timeTables.addAll(createTimeTablesForADay(storeId, scheduleId, dateOperation, timeOperation, dateTable));
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
            timeTablesForADay.add(TimeTable.create(storeId, scheduleId, dateTable.getId(), dateOperation.getId(), timeOperation.getId(), dateTable.getDate(), currentTime, 0));
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


    /**
     * 매장의 기본 운영 방식을 조회합니다.
     */
    public ScheduleResponse getScheduleOperation(Long storeId) {

        // 1. schedule 조회
        // schedule 조회
        Schedule schedule = scheduleService.findByStoreId(storeId);

        // 2. dateOperation 조회 후 Time도 같이 조회해서 response
        List<DateOperation> dateOperations = dateOperationService.findByScheduleId(schedule.getId());

        List<DateOperationResponse> scheduleOperationResponses = dateOperations
                .stream()
                .map(DateOperationResponse::of)
                .collect(Collectors.toList());

        return ScheduleResponse.of(schedule, scheduleOperationResponses);
    }

    /**
     * 기본 운영 방식을 수정합니다.
     */
    @Transactional
    public void updateDateOperation(Long storeId, DateOperationUpdateRequests request) {

        List<Long> dateOperationIds = request.getDateOperations()
                .stream()
                .map(DateOperationUpdateRequest::getDateOperationId)
                .collect(Collectors.toList());

        // 기존 dateTable, timeTable 모두 expire
        dateTableService.bulkExpireByDateOperationIds(dateOperationIds);
        timeTableService.bulkExpireByDateOperationIds(dateOperationIds);

        // 날짜 중복 체크
        dateOperationService.validateDateOperation(request);

        // 시간 중복 체크
        dateOperationService.validateTimeOperation(request.getDateOperations());

        for(DateOperationUpdateRequest req : request.getDateOperations()) {
            // 1. dateOperation 조회
            DateOperation dateOperation = dateOperationService.findById(req.getDateOperationId());

            // 2. operation 수정
            dateOperation.update(req.getStartDate(), req.getEndDate());

            // 3. operationTime 모두 삭제/ 재생성
            List<TimeOperation> createTimeOperations = new ArrayList<>();
            for (TimeOperationUpdateRequest time : req.getTimeOperations()) {
                createTimeOperations.add(TimeOperation.create(dateOperation.getId(), time));
            }
            timeOperationService.bulkExpire(
                    dateOperation.getTimeOperations()
                            .stream()
                            .map(TimeOperation::getId)
                            .collect(Collectors.toList())
            );
            List<TimeOperation> createdTimeOperations = timeOperationService.save(createTimeOperations);

            saveTimeTableBetweenStartAndEnd(storeId, dateOperation.getScheduleId(), dateOperation, createdTimeOperations, dateOperation.getEndDate(), dateOperation.getStartDate());
        }
    }

    /*
     * 기본 운영 방식을 삭제합니다.
    */
    @Transactional
    public void deleteDateOperation(Long dateOperationId) {

        DateOperation dateOperation = dateOperationService.findById(dateOperationId);
        dateOperation.expire();

        List<Long> toIds = dateOperation.getTimeOperations()
                .stream()
                .map(TimeOperation::getId)
                .collect(Collectors.toList());

        timeOperationService.bulkExpire(toIds);

        List<Long> ids = Collections.singletonList(dateOperationId);
        // date/ timeTable 조정
        dateTableService.bulkExpireByDateOperationIds(ids);
        timeTableService.bulkExpireByDateOperationIds(ids);
    }

    /*
     * 예약 스케줄 기간 조회를 합니다 + 해당 일자의 타임테이블을 조회합니다.
    */
    public List<DateTableResponse> getTimeTable(Long storeId, SearchTimeTableRequest request) {

        List<TimeTableWithDateTableDto> timeTableDtos = timeTableService.search(storeId, request.getStartDate(), request.getEndDate());

        if(timeTableDtos.isEmpty()) {
            throw new NotSearchTimeTableException();
        }

        Map<Long, List<TimeTableWithDateTableDto>> map = timeTableDtos
                .stream()
                .collect(Collectors.groupingBy(TimeTableWithDateTableDto::getDateTableId));

        List<Long> timeTableIds = timeTableDtos.stream()
                        .map(TimeTableWithDateTableDto::getTimeTableId)
                        .collect(Collectors.toList());
        List<Reservation> reservations = reservationService.getActive(timeTableIds);
        Map<Long, List<Reservation>> reservationMap = reservations
                .stream()
                .collect(Collectors.groupingBy(Reservation::getTimeTableId));

        List<DateTableResponse> responses = new ArrayList<>();
        map.forEach((key, value) -> {
            responses.add(DateTableResponse.of(key, value, reservationMap));
        });

        // 매장의 휴일 목록
        List<LocalDate> holidayDates = getHolidayDates(storeId, request.getStartDate(), request.getEndDate());

        // 휴무일이면 response 내 isHoliday = true 로 수정
        responses
                .stream()
                .filter(response -> holidayDates.contains(response.getDate()))
                .forEach(response -> response.changeIsHoliday(true));

        return responses
                .stream()
                .sorted(Comparator.comparing(DateTableResponse::getDate))
                .collect(Collectors.toList());
    }

    /*
     * 한 번에 예약 가능한 인원 수정을 합니다.
     */
    @Transactional
    public void updateScheduleLimitCount(Long storeId, ScheduleLimitCountRequest request) {

        // TODO: validation : min <= max

        Schedule schedule = scheduleService.findByStoreId(storeId);

        schedule.updateLimitCount(request.getRequestMinPerson(), request.getRequestMaxPerson());
    }

    /**
     * 당일 예약 관련 설정 수정을 합니다.
     */
    @Transactional
    public void updateScheduleSameDay(Long storeId, ScheduleSameDayRequest request) {

        Schedule schedule = scheduleService.findByStoreId(storeId);

        schedule.updateScheduleSameDay(request.getSameDayRequestApproval(), request.getSameDayCancelApproval());
    }

    /*
     * 해당 dateTable 및 dateTable 내 timeTable 수정
    */
    @Transactional
    public void updateTimeTable(TimeTablesRequest request) {

        DateTable dateTable = dateTableService.findById(request.getDateTableId());
        DateOperation dateOperation = dateOperationService.findById(dateTable.getDateOperationId());
        Schedule schedule = scheduleService.findById(dateTable.getScheduleId());

        // 예약 받기 미사용
        if(!request.isDailyAvailable()) {
            dateTable.updateDailyAvailable(false);
            return;
        }

        // 예약 받기 사용
        // dateTable - 예약 받기, 하루 총 인원 수정
        dateTable.update(request.isDailyAvailable(), request.getMaxPersonOfDay(), request.getIsHourlySetting());

        List<TimeTable> timeTables = dateTable.getTimeTables()
                .stream()
                .filter(t -> Objects.isNull(t.getExpiredAt()))
                .collect(Collectors.toList());

        // 기존 timeTable - 활성여부, 인원수 수정
        Map<Long, TimeTable> timeTableMap = timeTables
                .stream()
                .collect(Collectors.toMap(TimeTable::getId, Function.identity(), (existing, replacement) -> existing));
        Map<Long, TimeTableRequest> requestTimeTableMap = request.getTimeTables()
                .stream()
                .collect(Collectors.toMap(TimeTableRequest::getTimeTableId, Function.identity(), (existing, replacement) -> existing));

        requestTimeTableMap.forEach((key, value) -> {
            TimeTable tt = timeTableMap.get(key);
            if(Objects.nonNull(tt)) {
                tt.update(value);
            }
        });

        // 검증을 위해 같은 일자 다른 dateTable 조회
        List<DateTable> remainingDateTables = dateTableService.getOthersInDate(dateTable);

        // timeTable 수정 검증
        if (!validateTimeTableModification(timeTables, remainingDateTables, request.getStartTime(), request.getEndTime(), dateOperation.getTimeUnit())) return;

        // start/ endTime -> time_table 리스트에 반영
        // startTime 이전 시간 또는 endTime 이후 시간의 timeTable expire 처리
        // TODO: filter 부분 Predicate 로 수정
        List<Long> expireIds = timeTables.stream()
                .filter(tt -> tt.getTime().isBefore(request.getStartTime())
                        || tt.getTime().isAfter(request.getEndTime())
                        || tt.getTime().equals(request.getEndTime()))
                .map(TimeTable::getId)
                .collect(Collectors.toList());
        if(!expireIds.isEmpty()) {
            timeTableService.bulkExpireByIds(expireIds);
        }

        // startTime, endTime 시간 내 조회되지 않는 timeTable 은 생성
        List<TimeTable> createTimeTables = new ArrayList<>();
        LocalTime currentTime = request.getStartTime();
        List<LocalTime> times = timeTables
                .stream()
                .map(TimeTable::getTime)
                .toList();

        TimeOperation timeOperation = timeOperationService.findById(dateTable.getTimeOperationId());
        while (currentTime.isBefore(request.getEndTime())) {
            if(!times.contains(currentTime)) {
                createTimeTables.add(TimeTable.create(dateTable.getStoreId(), schedule.getId(), dateTable.getId(), dateOperation.getId(), timeOperation.getId(), dateTable.getDate(), currentTime, 0));
            }

            currentTime = getCurrentTimeByTimeUnit(dateOperation, currentTime);
        }
        timeTableService.save(createTimeTables);
    }

    public boolean validateTimeTableModification(
            List<TimeTable> timeTables,
            List<DateTable> remainingDateTables,
            LocalTime startTime,
            LocalTime endTime,
            TimeUnitType timeUnit
    ) {

        // 기존 timeTable 과 startTime, endTime 같다면 early return; 기존 것 그대로 들어왔기 때문에 변경 없음
        if (!timeTables.isEmpty()) {
            LocalTime minTime = makeStartDateOfDateTable(timeTables);
            LocalTime maxTime = makeEndDateOfDateTable(timeTables, timeUnit);
            if (startTime.equals(minTime)
                    && endTime.equals(maxTime)) {
                return false;
            }
        }

        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new NotAllowedTimeException();
        }

        List<Long> remainIds = remainingDateTables.stream()
                        .map(DateTable::getDateOperationId)
                        .toList();
        List<DateOperation> dateOperations = dateOperationService.getDateOperationsByDateTableIds(remainIds);

        Map<Long, DateOperation> dateOperationMap = dateOperations.stream()
                        .collect(Collectors.toMap(BaseEntity::getId, Function.identity(), (s1, s2) -> s1));

        remainingDateTables.stream()
                .filter(r -> {
                    LocalTime minTime = makeStartDateOfDateTable(r.getTimeTables());
                    LocalTime maxTime = makeEndDateOfDateTable(r.getTimeTables(), dateOperationMap.get(r.getDateOperationId()).getTimeUnit());
                    return !validateTime(startTime, endTime, minTime, maxTime);
                })
                .findFirst()
                .ifPresent(index -> {
                    throw new OverlapTimeOperationException();
                });

        return true;
    }

    // Holiday 의 일자 가져오기
    private List<LocalDate> getHolidayDates(Long storeId, LocalDate startDate, LocalDate endDate) {

        List<OtherHoliday> otherHolidays = otherHolidayService.searchByStoreId(storeId, startDate, endDate);
        Set<LocalDate> dates = new HashSet<>(getDatesOfOtherHolidays(otherHolidays));

        RegularHoliday regularHoliday = regularHolidayService.findByStoreId(storeId);
        dates.addAll(getDatesOfRegularHoliday(regularHoliday, startDate, endDate));

        return new ArrayList<>(dates);
    }

    private List<LocalDate> getDatesOfOtherHolidays(List<OtherHoliday> otherHolidays) {

        Set<LocalDate> dates = new HashSet<>();

        for (OtherHoliday otherHoliday : otherHolidays) {
            LocalDate startDate = otherHoliday.getStartDate();
            LocalDate endDate = otherHoliday.getEndDate();

            while (!startDate.isAfter(endDate)) {
                dates.add(startDate);
                startDate = startDate.plusDays(1);
            }
        }
        return dates.stream().toList();
    }

    private List<LocalDate> getDatesOfRegularHoliday(RegularHoliday regularHoliday, LocalDate startDate, LocalDate endDate) {

        List<LocalDate> dates = new ArrayList<>();

        List<String> dayOfWeeks = regularHoliday.getDayOfWeekList();

        while (!startDate.isAfter(endDate)) {
            if (dayOfWeeks.contains(startDate.getDayOfWeek().toString())) {
                dates.add(startDate);
            }
            startDate = startDate.plusDays(1);
        }

        return dates;
    }

}
