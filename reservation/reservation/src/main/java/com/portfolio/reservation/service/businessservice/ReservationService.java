package com.portfolio.reservation.service.businessservice;

import com.portfolio.reservation.domain.reservation.Reservation;
import com.portfolio.reservation.domain.reservation.ReservationStatus;
import com.portfolio.reservation.domain.schedule.Schedule;
import com.portfolio.reservation.domain.schedule.type.SameDayApprovalType;
import com.portfolio.reservation.domain.store.Store;
import com.portfolio.reservation.domain.timetable.DateTable;
import com.portfolio.reservation.domain.timetable.TimeTable;
import com.portfolio.reservation.dto.reservation.AvailableTimeDto;
import com.portfolio.reservation.dto.reservation.TimeTableDto;
import com.portfolio.reservation.repository.reservation.ReservationRepository;
import com.portfolio.reservation.repository.reservation.ReservationRepositoryCustom;
import com.portfolio.reservation.service.datetable.DateTableService;
import com.portfolio.reservation.service.holiday.HolidayService;
import com.portfolio.reservation.service.schedule.ScheduleService;
import com.portfolio.reservation.service.store.StoreService;
import lombok.RequiredArgsConstructor;
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

    DateTimeFormatter yyyyMMddFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    // 예약 인원 차지 상태 반환
    public List<Reservation> getActive(List<Long> timeTableIds) {

        return reservationRepository.getActive(timeTableIds, ReservationStatus.getImpossibleReservations());
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

        Map<DateTable, Long> personDateMap = reservationRepositoryCustom.countByDateAndStatusesGroupByDateTable(scheduleId, dates, ReservationStatus.getImpossibleReservations(), reserveId);

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

        Map<LocalDateTime, Long> personTimeMap = reservationRepositoryCustom.countByTimeAndStatusesGroupByTime(scheduleId, times, ReservationStatus.getImpossibleReservations(), reserveId);

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

    // TODO: 예약

    // TODO: 예약 상태 관리
}
