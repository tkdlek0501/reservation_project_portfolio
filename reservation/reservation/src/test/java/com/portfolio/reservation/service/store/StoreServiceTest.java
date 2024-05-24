package com.portfolio.reservation.service.store;

import com.portfolio.reservation.domain.store.Store;
import com.portfolio.reservation.domain.user.AuthorityType;
import com.portfolio.reservation.domain.user.User;
import com.portfolio.reservation.dto.store.StoreCreateRequest;
import com.portfolio.reservation.dto.store.StoreResponse;
import com.portfolio.reservation.dto.store.StoreUpdateRequest;
import com.portfolio.reservation.dto.user.UserCreateRequest;
import com.portfolio.reservation.exception.store.NotFoundStoreException;
import com.portfolio.reservation.exception.user.NotFoundUserException;
import com.portfolio.reservation.repository.store.StoreRepository;
import com.portfolio.reservation.repository.user.UserRepository;
import com.portfolio.reservation.service.user.UserService;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
public class StoreServiceTest {

    @Autowired
    StoreService storeService;

    @Autowired
    UserService userService;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @Autowired
    PasswordEncoder passwordEncoder;

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

    // 통과
    @Test
    public void create_테스트() throws Exception {

        // given
        User user = setUser();

        StoreCreateRequest request = new StoreCreateRequest(
               "매장1"
        );

        // when
        storeService.create(request);

        // then
        Store createdStore = storeRepository.findTopByOrderById()
                .orElseThrow(NotFoundStoreException::new);
       assertThat(createdStore.getName()).isEqualTo("매장1");
       assertThat(createdStore.getUserId()).isEqualTo(user.getId());
    }

    // 통과
    @Test
    public void update_테스트() throws Exception {

        // given
        create_테스트();

        StoreUpdateRequest request = new StoreUpdateRequest(
                "매장2"
        );

        // when
        storeService.update(request);

        // then
        Store updatedStore = storeRepository.findTopByOrderById()
                .orElseThrow(NotFoundStoreException::new);
        assertThat(updatedStore.getName()).isEqualTo("매장2");
    }

    // 통과
    @Test
    public void delete_테스트() throws Exception {

        // given
        create_테스트();

        // when
        storeService.delete("12345");

        // then
        Store store = storeRepository.findTopByOrderById()
                .orElseThrow();

        assertThat(store.getExpiredAt()).isNotNull();
    }

    // 통과
    @Test
    public void getMyStore_테스트() throws Exception {

        // given
        create_테스트();

        // when
        StoreResponse response = storeService.getMyStore();

        // then
        Store store = storeRepository.findTopByOrderById()
                .orElseThrow(NotFoundStoreException::new);
        User user = userRepository.findTopByOrderById()
                .orElseThrow(NotFoundUserException::new);

        assertThat(response.getStoreId()).isEqualTo(store.getId());
        assertThat(response.getStoreName()).isEqualTo(store.getName());
        assertThat(response.getNickName()).isEqualTo(user.getNickname());
    }

    // 통과
    @Test
    public void getStore_테스트() throws Exception {

        // given
        create_테스트();

        // when
        Store store = storeRepository.findTopByOrderById()
                .orElseThrow(NotFoundStoreException::new);
        StoreResponse response = storeService.getStore(store.getId());

        // then
        User user = userRepository.findTopByOrderById()
                .orElseThrow(NotFoundUserException::new);
        assertThat(response.getStoreId()).isEqualTo(store.getId());
        assertThat(response.getStoreName()).isEqualTo(store.getName());
        assertThat(response.getNickName()).isEqualTo(user.getNickname());
    }
}
