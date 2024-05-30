package com.portfolio.reservation.service.schedule;

import com.portfolio.reservation.domain.schedule.Schedule;
import com.portfolio.reservation.domain.schedule.type.SameDayApprovalType;
import com.portfolio.reservation.domain.store.Store;
import com.portfolio.reservation.domain.user.User;
import com.portfolio.reservation.exception.schedule.NotFoundScheduleException;
import com.portfolio.reservation.repository.schedule.ScheduleRepository;
import com.portfolio.reservation.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final StoreService storeService;

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

}
