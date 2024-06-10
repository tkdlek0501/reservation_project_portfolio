package com.portfolio.reservation.service.holiday;

import com.portfolio.reservation.domain.schedule.OtherHoliday;
import com.portfolio.reservation.repository.holiday.OtherHolidayRepository;
import com.portfolio.reservation.repository.holiday.OtherHolidayRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OtherHolidayService {

    private final OtherHolidayRepository otherHolidayRepository;
    private final OtherHolidayRepositoryCustom otherHolidayRepositoryCustom;

    public List<OtherHoliday> searchByStoreId(Long storeId, LocalDate startDate, LocalDate endDate) {

        return otherHolidayRepositoryCustom.searchByStoreId(storeId, startDate, endDate);
    }


}
