package com.portfolio.reservation.service;

import com.portfolio.reservation.domain.user.AuthorityType;
import com.portfolio.reservation.domain.user.User;
import com.portfolio.reservation.dto.user.UserCreateRequest;
import com.portfolio.reservation.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void init() {
        flushAndclear();
    }

    private void flushAndclear() {
        em.flush();
        em.clear();
    }

    @Test
    public void signUp_테스트() throws Exception {

        // given
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
        User createdUser = userRepository.findTopByOrderById()
                .orElseThrow(() -> new Exception("생성된 user를 찾을 수 없습니다."));

        // then
        if(!createdUser.matchPassword(passwordEncoder, password)) {
            throw new Exception("패스워드가 다릅니다.");
        }

        assertThat(createdUser.getUsername()).isEqualTo(username);
        assertThat(createdUser.getNickname()).isEqualTo(nickname);
        assertThat(createdUser.getAuthority()).isEqualTo(authorityType);
    }
}
