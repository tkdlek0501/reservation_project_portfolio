package com.portfolio.reservation.service.holiday;

import com.portfolio.reservation.domain.schedule.RegularHoliday;
import com.portfolio.reservation.repository.holiday.RegularHolidayRepository;
import com.portfolio.reservation.repository.holiday.RegularHolidayRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegularHolidayService {

    private final RegularHolidayRepository regularHolidayRepository;
    private final RegularHolidayRepositoryCustom regularHolidayRepositoryCustom;

    public RegularHoliday findByStoreId(Long storeId) {

        return regularHolidayRepositoryCustom.findByStoreId(storeId);
    }
}
