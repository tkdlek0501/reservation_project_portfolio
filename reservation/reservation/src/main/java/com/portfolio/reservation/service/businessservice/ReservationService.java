package com.portfolio.reservation.service.businessservice;

import com.portfolio.reservation.domain.reservation.Reservation;
import com.portfolio.reservation.domain.reservation.ReservationHistory;
import com.portfolio.reservation.domain.reservation.ReservationStatus;
import com.portfolio.reservation.domain.schedule.Schedule;
import com.portfolio.reservation.domain.schedule.type.SameDayApprovalType;
import com.portfolio.reservation.domain.store.Store;
import com.portfolio.reservation.domain.timetable.DateTable;
import com.portfolio.reservation.domain.timetable.TimeTable;
import com.portfolio.reservation.dto.reservation.*;
import com.portfolio.reservation.exception.reservation.*;
import com.portfolio.reservation.repository.reservation.ReservationRepository;
import com.portfolio.reservation.repository.reservation.ReservationRepositoryCustom;
import com.portfolio.reservation.service.datetable.DateTableService;
import com.portfolio.reservation.service.holiday.HolidayService;
import com.portfolio.reservation.service.reservationhistory.ReservationHistoryService;
import com.portfolio.reservation.service.schedule.ScheduleService;
import com.portfolio.reservation.service.store.StoreService;
import com.portfolio.reservation.service.timetable.TimeTableService;
import com.portfolio.reservation.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationRepositoryCustom reservationRepositoryCustom;
    private final ScheduleService scheduleService;
    private final DateTableService dateTableService;
    private final HolidayService holidayService;
    private final TimeTableService timeTableService;
    private final ReservationHistoryService reservationHistoryService;
    private final UserService userService;
    private final StoreService storeService;

    DateTimeFormatter yyyyMMddFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    // 예약 인원 차지 상태 반환
    public List<Reservation> getActive(List<Long> timeTableIds) {

        return reservationRepository.getActive(timeTableIds, ReservationStatus.getOccupiedReservations());
    }

    /**
     * 예약 가능정보를 조회합니다.(예약 가능 시간 조회)
     */
    public AvailableTimeDto getAvailableReservation(Long storeId, Long scheduleId, String stringDate, Long reserveId) {

        Schedule schedule = scheduleService.findById(scheduleId);

        LocalDate dateCondition = LocalDate.parse(stringDate, yyyyMMddFormatter);

        // 날짜 기준 모든 dataTable
        List<DateTable> dateTables = dateTableService.findAllByDate(scheduleId, true, dateCondition);

        List<LocalDate> dates = dateTables.stream().map(DateTable::getDate).collect(Collectors.toList());

        Map<DateTable, Long> personDateMap = reservationRepositoryCustom.countByDateAndStatusesGroupByDateTable(scheduleId, dates, ReservationStatus.getOccupiedReservations(), reserveId);

        List<LocalTime> times = new ArrayList<>();

        for (DateTable dateTable : dateTables) {
            List<TimeTable> timeTables = dateTable.getTimeTables()
                    .stream()
                    .filter(t -> Objects.isNull(t.getExpiredAt()))
                    .toList();
            for (TimeTable timeTable : timeTables) {
                times.add(timeTable.getTime());
            }
        }
        times = times
                .stream()
                .distinct()
                .collect(Collectors.toList());

        Map<LocalDateTime, Long> personTimeMap = reservationRepositoryCustom.countByTimeAndStatusesGroupByTime(scheduleId, times, ReservationStatus.getOccupiedReservations(), reserveId);

        // 매장의 휴일 목록
        List<LocalDate> holidayDates = holidayService.getHolidayDates(storeId, dateCondition, dateCondition);

        // 일자 제외
        List<DateTable> removeDateTables = dateTables.stream()
                .filter(dt ->
                        holidayDates.contains(dt.getDate()) // 휴일에 포함돼있거나
                        || personDateMap.getOrDefault(dt, 0L) >= dt.getMaxPerson() // 최대 인원 이상이거나
                        || dt.getTimeTables().stream().noneMatch(tt -> Objects.isNull(tt.getExpiredAt())) // 삭제되지 않은 타임테이블이 없거나
                        || dt.getDate().isBefore(LocalDate.now()) // 오늘 이전 날짜이면
                )
                .toList();
        dateTables.removeAll(removeDateTables);

        // time 관련 아래서 계산
        List<TimeTableDto> possibleTimes = new ArrayList<>();
        for (DateTable dateTable : dateTables) {
            Long dateReservationPersons = personDateMap.getOrDefault(dateTable, 0L);
            Long maxPerson = dateTable.getMaxPerson() - dateReservationPersons;
            List<TimeTable> timeTables;

            if (dateTable.isHourlySetting()) {
                timeTables = dateTable.getTimeTables()
                        .stream()
                        .filter(t -> t.isAvailable()
                                && Objects.isNull(t.getExpiredAt()))
                        .collect(Collectors.toList());
            } else {
                timeTables = dateTable.getTimeTables()
                        .stream()
                        .filter(t -> Objects.isNull(t.getExpiredAt()))
                        .collect(Collectors.toList());
            }

            for (TimeTable timeTable : timeTables) {

                // 당일 설정에 따른 필터링
                if (!validateSameDay(timeTable, schedule)) {
                    continue;
                }

                // 시간별 설정시 person 계산
                if (dateTable.isHourlySetting()) {
                    Long timeReservationPersons = personTimeMap.get(LocalDateTime.of(dateTable.getDate(), timeTable.getTime())) == null ?
                            0 : personTimeMap.get(LocalDateTime.of(dateTable.getDate(), timeTable.getTime()));

                    if (timeTable.getMaxPerson() < 0 || timeReservationPersons < timeTable.getMaxPerson()
                    ) {
                        maxPerson = timeTable.getMaxPerson() - timeReservationPersons < maxPerson ?
                                timeTable.getMaxPerson() - timeReservationPersons : maxPerson;
                        possibleTimes.add(TimeTableDto.of(timeTable, maxPerson));
                    }
                } else {
                    possibleTimes.add(TimeTableDto.of(timeTable, maxPerson));
                }
            }
        }

        return AvailableTimeDto.of(possibleTimes, schedule.getRequestMaxPerson());
    }

    /**
     * 오늘 날짜의 예약에 대해서 당일 설정에 따라 validation 합니다.
     */
    public static boolean validateSameDay(TimeTable timeTable, Schedule schedule) {

        SameDayApprovalType sameDayRequest = schedule.getSameDayRequestApproval();

//      당일 예약 설정에 걸리지 않으면 true
        return validateSameDayOfNowDate(timeTable.getDate()) // 오늘 날짜 아니면 아래 모두 true 이어야 true
                || (validateSameDayOfNowTime(timeTable.getTime())
                && validateSameDayOfNoneType(sameDayRequest)
                && validateSameDayOfHourType(sameDayRequest, timeTable.getTime()));
    }

    // 오늘 날짜가 아니면 통과
    public static boolean validateSameDayOfNowDate(LocalDate rawDate) {

        return !rawDate.equals(LocalDate.now());
    }

    // 예약 시간이 현재 시간을 지나지 않으면 통과
    public static boolean validateSameDayOfNowTime(LocalTime rawTime) {

        return !rawTime.isBefore(LocalTime.now());
    }

    // 당일 예약 사용이 불가가 아니면 통과
    public static boolean validateSameDayOfNoneType(SameDayApprovalType sameDayType) {

        return !sameDayType.equals(SameDayApprovalType.NONE);
    }

    // 당일 예약 사용 가능일 때, 예약 가능 시간 지나지 않았으면 통과
    public static boolean validateSameDayOfHourType(SameDayApprovalType sameDayType, LocalTime rawTime) {

        int hour = sameDayType.getHour();

        return !(!sameDayType.equals(SameDayApprovalType.POSSIBLE)
                && rawTime.minusHours(hour).isBefore(LocalTime.now()));
    }

    /**
     * 예약 요청
     */
    @Transactional
    public void createUserReservation(Long storeId, ReservationCreateRequest request) {

        // timeTable 찾기
        TimeTable timeTable = timeTableService.findById(request.getTimeId());

        // validation
        validateForReservation(storeId, request.getPersonCount(), timeTable, null);

        Long userId = userService.getMe().getUserId();

        // reservation 등록
        Reservation reservation = reservationRepository.save(Reservation.create(
                userId,
                storeId,
                timeTable.getId(),
                LocalDateTime.of(timeTable.getDate(), timeTable.getTime()),
                request.getPersonCount(),
                timeTable.getDate(),
                timeTable.getTime(),
                request.getReserveRequest()
        ));

        // log 에 추가
        reservationHistoryService.save(ReservationHistory.create(
            reservation.getId(),
            userId,
            timeTable.getId(),
            ReservationStatus.RESERVE_CONFIRM,
            timeTable.getDate(),
            timeTable.getTime(),
            request.getPersonCount(),
            request.getReserveRequest()
        ));
    }

    /**
     * 예약 등록 유효성 검사
     */
    private Schedule validateForReservation(Long storeId, int persons, TimeTable timeTable, Long reservationId) {

        // 휴무일 체크
        List<LocalDate> holidayDates = holidayService.getHolidayDates(storeId, timeTable.getDate(), timeTable.getDate());
        if (!holidayDates.isEmpty()) {
            throw new NotAllowedReservationAboutHolidayException();
        }

        // dateTable 기준 인원 확인
        // 이미 예약된 인원
        Map<DateTable, Long> personDateMap = reservationRepositoryCustom.countByDateAndStatusesGroupByDateTable(timeTable.getScheduleId(), List.of(timeTable.getDate()), ReservationStatus.getOccupiedReservations(), reservationId);
        DateTable dateTable = dateTableService.findById(timeTable.getDateTableId());
        long dateReservationPersons = personDateMap.getOrDefault(dateTable, 0L);
        // dateTable 에서 허용하는 최대 인원
        int maxPersonOfDateTable = dateTable.getMaxPerson();

        if (!validateDateReservationPerson(persons, dateReservationPersons, maxPersonOfDateTable)) {
            throw new NotAllowedReservationAboutMaxPersonOfDateTableException();
        }

        // time 개별 설정 사용시 time 최대 인원 확인
        if (dateTable.isHourlySetting()) {
            // 이미 예약된 인원
            Map<LocalDateTime, Long> personTimeMap = reservationRepositoryCustom.countByTimeAndStatusesGroupByTime(timeTable.getScheduleId(), List.of(timeTable.getTime()), ReservationStatus.getOccupiedReservations(), reservationId);
            long timeReservationPersons = personTimeMap.getOrDefault(LocalDateTime.of(timeTable.getDate(), timeTable.getTime()), 0L);

            if (!validateTimeReservationPerson(persons, timeReservationPersons, timeTable.getMaxPerson())) {
                throw new NotAllowedReservationAboutMaxPersonOfTimeTableException();
            }
        }

        // schedule
        Schedule schedule = scheduleService.findById(timeTable.getScheduleId());

        // 한 번에 예약 받을 수 있는 인원
        if (!validateRequestMinPerson(persons, schedule.getRequestMinPerson())) {
            throw new NotAllowedReservationAboutRequestMinPersonException();
        } else if (!validateRequestMaxPerson(persons, schedule.getRequestMaxPerson())) {
            throw new NotAllowedReservationAboutRequestMaxPersonException();
        }

        // 당일 예약 필터링
        if (!validateSameDayOfNowDate(timeTable.getDate())) { // 오늘 날짜이면
            if (!validateSameDayOfNowTime(timeTable.getTime())) {
                throw new NotAllowedReservationAboutSameTimeException();
            }
            SameDayApprovalType sameDayRequest = schedule.getSameDayRequestApproval();
            if (!validateSameDayOfNoneType(sameDayRequest)) {
                throw new NotAllowedReservationAboutSameDateException();
            }
            if (!validateSameDayOfHourType(sameDayRequest, timeTable.getTime())) {
                throw new NotAllowedReservationAboutSameDayTypeException(sameDayRequest.getHour());
            }
        }

        return schedule;
    }

    /**
     * 해당 dataTable 의 예약 인원 validation 을 합니다.
     */
    public static boolean validateDateReservationPerson(int persons, long dateReservationPersons, int maxPersonOfDay) {

        return dateReservationPersons + persons <= maxPersonOfDay;
    }

    /**
     * dateTable의 hourlySetting에 따라 예약 인원 validation을 합니다.
     */
    public static boolean validateTimeReservationPerson(int persons, long timeReservationPersons, int maxPersonOfTime) {

//        시간별 설정이 돼있을 때, 해당 시간 예약 인원 조건을 통과하면 true
        return maxPersonOfTime < 0 || timeReservationPersons + persons <= maxPersonOfTime;
    }

    /**
     * 요청 인원이 한 번에 예약 가능한 최소 인원보다 크면 true
     */
    public static boolean validateRequestMinPerson(int persons, int requestMinPersons) {

        return persons >= requestMinPersons;
    }

    /**
     * 요청 인원이 한 번에 예약 가능한 최소 인원보다 크면 true
     */
    public static boolean validateRequestMaxPerson(int persons, int requestMaxPersons) {

        return persons <= requestMaxPersons;
    }

    /**
     * 예약 변경
     */
    @Transactional
    public void updateUserReservation(Long storeId, ReservationModifyRequest request) {

        // 기존 reservation 찾아와서 변경 요청 가능한 상태인지 확인
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(NotFoundReservationException::new);

        if (!ReservationStatus.getPossibleModify().contains(reservation.getStatus())){
            throw new NotAllowedReservationAboutStatusException();
        }

        // 이전 예약
        TimeTable orgTimeTable = timeTableService.findById(reservation.getTimeTableId());

        // 이전 예약과 같은 내용인지 확인
        if(orgTimeTable.getId().equals(request.getTimeId())
            && reservation.getPersons() == request.getPersonCount()
            && Objects.equals(reservation.getLastReason(), request.getReserveRequest())
        ) {
            throw new NotAllowedSameReservationException();
        }

        TimeTable timeTable = timeTableService.findById(request.getTimeId());

        // 공통 validation
        validateForReservation(storeId, request.getPersonCount(), timeTable, request.getReservationId());

        // reservation 상태 업데이트; 예약 변경 요청 (아직 변경 되면 안됨 승인 후 변경돼야 함)
        reservation.updateToChangeRequest();

        Long userId = userService.getMe().getUserId();

        // log 에 추가
        reservationHistoryService.save(ReservationHistory.create(
                reservation.getId(),
                userId,
                timeTable.getId(),
                ReservationStatus.CHANGE_REQUEST,
                timeTable.getDate(),
                timeTable.getTime(),
                request.getPersonCount(),
                request.getReserveRequest()
        ));
    }

    /**
     * 예약 취소
     */
    @Transactional
    public void deleteReservation(Long storeId, ReservationCancelRequest request) {

        Long userId = userService.getMe().getUserId();

        // 기존 reservation 찾아와서 취소 가능한 상태인지 확인
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(NotFoundReservationException::new);

        if (!ReservationStatus.getPossibleModify().contains(reservation.getStatus())){
            throw new NotAllowedReservationAboutStatusException();
        }

        TimeTable timeTable = timeTableService.findById(reservation.getTimeTableId());
        Schedule schedule = scheduleService.findById(timeTable.getScheduleId());

        // 당일 예약 필터링
        if (!validateSameDayOfNowDate(timeTable.getDate())) { // 오늘 날짜이면
            if (!validateSameDayOfNowTime(timeTable.getTime())) {
                throw new NotAllowedReservationAboutSameTimeException();
            }
            SameDayApprovalType sameDayRequest = schedule.getSameDayRequestApproval();
            if (!validateSameDayOfNoneType(sameDayRequest)) {
                throw new NotAllowedReservationAboutSameDateException();
            }
            if (!validateSameDayOfHourType(sameDayRequest, timeTable.getTime())) {
                throw new NotAllowedReservationAboutSameDayTypeException(sameDayRequest.getHour());
            }
        }

        // reservation 상태 업데이트; 예약 취소 요청
        reservation.updateToCancelConfirm();

        // log 에 추가
        reservationHistoryService.save(ReservationHistory.create(
                reservation.getId(),
                userId,
                timeTable.getId(),
                ReservationStatus.CANCEL_CONFIRM,
                timeTable.getDate(),
                timeTable.getTime(),
                reservation.getPersons(),
                request.getCancelReason()
        ));
    }

    /**
     * 매장 예약 조회
     */
    public Page<ReservationResponse> findReservation(SearchReservationRequest request, Pageable pageable) {

        Long userId = userService.getMe().getUserId();
        Store store = storeService.findByUserId(userId);

        ReservationSearchCondition condition = ReservationSearchCondition.
                of(store.getId(), request.getStartDate(), request.getEndDate(), request.getStatuses(), request.getKeyword(), request.getKeywordType(), null);

        Page<ReservationDto> reservations = reservationRepositoryCustom.searchReservationByCondition(condition, pageable);

        return reservations.map(ReservationResponse::of);
    }

    /**
    *  예약 상세 조회
     */
    public ReservationDetailResponse getReservationDetail(Long reservationId) {

        ReservationDetailDto dto = reservationRepositoryCustom.getDetail(reservationId);
        List<ReservationHistory> histories = reservationHistoryService.findByReservationId(reservationId);

        return ReservationDetailResponse.of(dto, histories);
    }

    /**
     *  본인 예약 조회
     */
    public Page<ReservationUserResponse> getMyReservation(SearchReservationRequest request, Pageable pageable) {

        Long userId = userService.getMe().getUserId();

        ReservationSearchCondition condition = ReservationSearchCondition.
                of(null, request.getStartDate(), request.getEndDate(), request.getStatuses(), request.getKeyword(), request.getKeywordType(), userId);

        Page<ReservationUserDto> reservations = reservationRepositoryCustom.searchMyReservationByCondition(condition, pageable);

        return reservations.map(ReservationUserResponse::of);
    }

    // TODO: 예약 상태 관리 ~

}
