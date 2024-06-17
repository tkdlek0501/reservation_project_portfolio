package com.portfolio.reservation.service.holiday;

import com.portfolio.reservation.domain.schedule.OtherHoliday;
import com.portfolio.reservation.domain.schedule.RegularHoliday;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HolidayService {

    private final OtherHolidayService otherHolidayService;
    private final RegularHolidayService regularHolidayService;

    // Holiday 의 일자 가져오기
    public List<LocalDate> getHolidayDates(Long storeId, LocalDate startDate, LocalDate endDate) {

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
