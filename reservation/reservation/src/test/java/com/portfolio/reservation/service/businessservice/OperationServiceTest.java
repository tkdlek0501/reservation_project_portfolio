package com.portfolio.reservation.service.businessservice;

import com.portfolio.reservation.domain.schedule.DateOperation;
import com.portfolio.reservation.domain.schedule.Schedule;
import com.portfolio.reservation.domain.schedule.TimeOperation;
import com.portfolio.reservation.domain.schedule.type.SameDayApprovalType;
import com.portfolio.reservation.domain.schedule.type.TimeUnitType;
import com.portfolio.reservation.domain.store.Store;
import com.portfolio.reservation.domain.timetable.DateTable;
import com.portfolio.reservation.domain.timetable.TimeTable;
import com.portfolio.reservation.domain.user.AuthorityType;
import com.portfolio.reservation.domain.user.User;
import com.portfolio.reservation.dto.operation.DateOperationUpdateRequest;
import com.portfolio.reservation.dto.operation.DateOperationUpdateRequests;
import com.portfolio.reservation.dto.operation.ScheduleLimitCountRequest;
import com.portfolio.reservation.dto.operation.ScheduleResponse;
import com.portfolio.reservation.dto.schedule.DateOperationRequest;
import com.portfolio.reservation.dto.schedule.TimeOperationRequest;
import com.portfolio.reservation.dto.store.StoreCreateRequest;
import com.portfolio.reservation.dto.user.UserCreateRequest;
import com.portfolio.reservation.exception.operation.NotFoundDateOperationException;
import com.portfolio.reservation.exception.store.NotFoundStoreException;
import com.portfolio.reservation.repository.dateoperation.DateOperationRepository;

import com.portfolio.reservation.repository.datetable.DateTableRepository;
import com.portfolio.reservation.repository.store.StoreRepository;
import com.portfolio.reservation.repository.timeoperation.TimeOperationRepository;
import com.portfolio.reservation.repository.timetable.TimeTableRepository;
import com.portfolio.reservation.repository.user.UserRepository;
import com.portfolio.reservation.service.dateoperation.DateOperationService;
import com.portfolio.reservation.service.schedule.ScheduleService;
import com.portfolio.reservation.service.store.StoreService;
import com.portfolio.reservation.service.user.UserService;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
public class OperationServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StoreService storeService;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    OperationService operationService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    DateOperationService dateOperationService;

    @Autowired
    DateOperationRepository dateOperationRepository;

    @Autowired
    TimeOperationRepository timeOperationRepository;

    @Autowired
    DateTableRepository dateTableRepository;

    @Autowired
    TimeTableRepository timeTableRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    public void init() {
        flushAndClear();
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }

    // 실제 로그인은 filter 로 처리하므로 대신 아래 메서드로 로그인
    private User setUser() throws Exception {
        String username = "username1";
        String password = "12345";
        String nickname = "nickname1";
        AuthorityType authorityType = AuthorityType.USER;

        UserCreateRequest request = new UserCreateRequest(
                username,
                password,
                nickname,
                authorityType
        );

        // when
        userService.signUp(request);

        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();

        emptyContext.setAuthentication(new UsernamePasswordAuthenticationToken(User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .authority(authorityType)
                .build(),
                null, null));

        SecurityContextHolder.setContext(emptyContext);

        return userRepository.findTopByOrderById()
                .orElse(null);
    }

    private void 운영방식생성() throws Exception {

        User user = setUser();
        StoreCreateRequest storeCreateRequest = new StoreCreateRequest(
                "매장1"
        );
        storeService.create(storeCreateRequest);
        Store store = storeRepository.findTopByOrderById()
                .orElseThrow(NotFoundStoreException::new);

        scheduleService.create(
                user,
                SameDayApprovalType.POSSIBLE,
                SameDayApprovalType.POSSIBLE,
                true,
                10,
                2
        );

        List<TimeOperationRequest> timeOperationRequests = new ArrayList<>();
        TimeOperationRequest timeOperationrequest = TimeOperationRequest.builder()
                .startTime(LocalTime.of(9,0))
                .endTime(LocalTime.of(13, 0))
                .maxPerson(10)
                .build();
        timeOperationRequests.add(timeOperationrequest);
        timeOperationrequest = TimeOperationRequest.builder()
                .startTime(LocalTime.of(14,0))
                .endTime(LocalTime.of(17, 0))
                .maxPerson(20)
                .build();
        timeOperationRequests.add(timeOperationrequest);

        DateOperationRequest request = DateOperationRequest.builder()
                .startDate(LocalDate.of(2024,6,11))
                .endDate(LocalDate.of(2024, 6, 30))
                .timeUnit(TimeUnitType.HOUR)
                .timeOperationRequests(timeOperationRequests)
                .build();

        operationService.createOperation(request);
        flushAndClear();
    }

    @Test
    public void createOperation_테스트() throws Exception {

        // given
        User user = setUser();
        StoreCreateRequest storeCreateRequest = new StoreCreateRequest(
                "매장1"
        );
        storeService.create(storeCreateRequest);
        Store store = storeRepository.findTopByOrderById()
                .orElseThrow(NotFoundStoreException::new);

        scheduleService.create(
                user,
                SameDayApprovalType.POSSIBLE,
                SameDayApprovalType.POSSIBLE,
                true,
                10,
                2
        );

        List<TimeOperationRequest> timeOperationRequests = new ArrayList<>();
        TimeOperationRequest timeOperationrequest = TimeOperationRequest.builder()
                .startTime(LocalTime.of(9,0))
                .endTime(LocalTime.of(13, 0))
                .maxPerson(10)
                .build();
        timeOperationRequests.add(timeOperationrequest);
        timeOperationrequest = TimeOperationRequest.builder()
                .startTime(LocalTime.of(14,0))
                .endTime(LocalTime.of(17, 0))
                .maxPerson(20)
                .build();
        timeOperationRequests.add(timeOperationrequest);

        DateOperationRequest request = DateOperationRequest.builder()
                .startDate(LocalDate.of(2024,6,11))
                .endDate(LocalDate.of(2024, 6, 30))
                .timeUnit(TimeUnitType.HOUR)
                .timeOperationRequests(timeOperationRequests)
                .build();

        // when
        operationService.createOperation(request);
        flushAndClear();

        // then
        DateOperation dateOperation = dateOperationRepository.findTopByOrderById()
                .orElseThrow(NotFoundDateOperationException::new);
        assertThat(dateOperation.getStartDate()).isEqualTo(LocalDate.of(2024,6,11));
        assertThat(dateOperation.getEndDate()).isEqualTo(LocalDate.of(2024, 6, 30));
        assertThat(dateOperation.getTimeUnit()).isEqualTo(TimeUnitType.HOUR);

        List<TimeOperation> timeOperations = dateOperation.getTimeOperations();
        assertThat(timeOperations).hasSize(2);
        assertThat(timeOperations.get(0).getStartTime()).isEqualTo(LocalTime.of(9,0));
        assertThat(timeOperations.get(1).getStartTime()).isEqualTo(LocalTime.of(14,0));
        assertThat(timeOperations.get(0).getEndTime()).isEqualTo(LocalTime.of(13, 0));
        assertThat(timeOperations.get(1).getEndTime()).isEqualTo(LocalTime.of(17, 0));

        // DateTable, TimeTable
        List<DateTable> dateTables = dateTableRepository.findAllByDateOperationId(dateOperation.getId());
        LocalDate minDate = dateTables.stream()
                .filter(dt -> Objects.isNull(dt.getExpiredAt()))
                .min(Comparator.comparing(DateTable::getDate))
                .map(DateTable::getDate)
                .orElse(null);
        LocalDate maxDate = dateTables.stream()
                .filter(dt -> Objects.isNull(dt.getExpiredAt()))
                .max(Comparator.comparing(DateTable::getDate))
                .map(DateTable::getDate)
                .orElse(null);
        assertThat(minDate).isEqualTo(LocalDate.of(2024, 6, 11));
        assertThat(maxDate).isEqualTo(LocalDate.of(2024, 6, 30));
        assertThat(dateTables.get(0).getStoreId()).isEqualTo(store.getId());
        assertThat(dateTables.get(0).getScheduleId()).isEqualTo(scheduleService.findByStoreId(store.getId()).getId());
        assertThat(dateTables.get(0).getTimeOperationId()).isEqualTo(timeOperations.get(0).getId());
        assertThat(dateTables.get(0).getMaxPerson()).isEqualTo(timeOperations.get(0).getMaxPerson());

        List<TimeTable> timeTables = dateTables.get(0).getTimeTables();
        LocalTime minTime = timeTables.stream()
                .filter(tt -> Objects.isNull(tt.getExpiredAt()))
                .min(Comparator.comparing(TimeTable::getTime))
                .map(TimeTable::getTime)
                .orElse(null);
        LocalTime maxTime = timeTables.stream()
                .filter(tt -> Objects.isNull(tt.getExpiredAt()))
                .max(Comparator.comparing(TimeTable::getTime))
                .map(TimeTable::getTime)
                .orElse(null);

        assertThat(timeTables).hasSize(4); // (9~13) 9, 10, 11, 12
        assertThat(minTime).isEqualTo(LocalTime.of(9, 0));
        assertThat(maxTime).isEqualTo(LocalTime.of(12, 0));

        timeTables = dateTables.get(1).getTimeTables();
        minTime = timeTables.stream()
                .filter(tt -> Objects.isNull(tt.getExpiredAt()))
                .min(Comparator.comparing(TimeTable::getTime))
                .map(TimeTable::getTime)
                .orElse(null);
        maxTime = timeTables.stream()
                .filter(tt -> Objects.isNull(tt.getExpiredAt()))
                .max(Comparator.comparing(TimeTable::getTime))
                .map(TimeTable::getTime)
                .orElse(null);

        assertThat(timeTables).hasSize(3); // (14~17) 14, 15, 16
        assertThat(minTime).isEqualTo(LocalTime.of(14, 0));
        assertThat(maxTime).isEqualTo(LocalTime.of(16, 0));
    }

    @Test
    public void getScheduleOperation_테스트() throws Exception {

        // given
        운영방식생성();

        // when
        Store store = storeRepository.findTopByOrderById()
                .orElseThrow(NotFoundStoreException::new);
        ScheduleResponse response = operationService.getScheduleOperation();
        Schedule schedule = scheduleService.findByStoreId(store.getId());

        // then
        assertThat(response.getRequestMaxPerson()).isEqualTo(schedule.getRequestMaxPerson());
        assertThat(response.getRequestMinPerson()).isEqualTo(schedule.getRequestMinPerson());
        assertThat(response.getSameDayCancelApproval()).isEqualTo(schedule.getSameDayCancelApproval());
        assertThat(response.getSameDayRequestApproval()).isEqualTo(schedule.getSameDayRequestApproval());
    }

    // TODO: 테스트 코드 작성
//    @Test
//    public void updateDateOperation_테스트() throws Exception {
//
//        // given
//        운영방식생성();
//        Store store = storeRepository.findTopByOrderById()
//                .orElseThrow(NotFoundStoreException::new);
//        List<DateOperationUpdateRequest> dateOperations = new ArrayList<>();
//        DateOperationUpdateRequest dateOperation = DateOperationUpdateRequest.builder()
//                .dateOperationId()
//
//        DateOperationUpdateRequests request = DateOperationUpdateRequests.builder()
//                                                    .dateOperations()
//                                                    .build();
//
//        // when
//        operationService.updateDateOperation(store.getId(), request);
//
//        // then
//    }

    // deleteDateOperation(Long dateOperationId)

    // List<DateTableResponse> getTimeTable(Long storeId, SearchTimeTableRequest request)
    // 조회는 테스트 코드 작성하지 않기

    // updateScheduleLimitCount(Long storeId, ScheduleLimitCountRequest request) {

    // updateScheduleSameDay(Long storeId, ScheduleSameDayRequest request) {

    // void updateTimeTable(TimeTablesRequest request) {
}
