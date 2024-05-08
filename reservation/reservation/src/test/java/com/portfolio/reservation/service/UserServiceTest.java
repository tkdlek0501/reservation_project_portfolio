package com.portfolio.reservation.service;

import com.portfolio.reservation.domain.user.AuthorityType;
import com.portfolio.reservation.domain.user.User;
import com.portfolio.reservation.dto.user.UserCreateRequest;
import com.portfolio.reservation.dto.user.UserResponse;
import com.portfolio.reservation.dto.user.UserUpdateRequest;
import com.portfolio.reservation.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.ExpressionException;
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

    // 통과
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

    // 통과
    @Test
    public void getUser_테스트() throws Exception {

        // given
        signUp_테스트();

        // when
        User user = userRepository.findById(1L)
                .orElse(null);

        // then
        assertThat(user).isNotNull();
    }

    // TODO: 아래부터는 로그인한 유저를 가져와야 해서 로그인 로직이 선행돼야 한다
    @Test
    public void update_테스트() throws Exception {

        // given
        UserUpdateRequest request = new UserUpdateRequest(
             "nickname2"
        );

        // when
        userService.update(request);
        flushAndclear();

        User updatedUser = userRepository.findTopByOrderById()
                .orElseThrow(() -> new Exception("생성된 user를 찾을 수 없습니다."));

        // then
        assertThat(updatedUser.getNickname()).isEqualTo("nickname2");
    }

    @Test
    public void updatePassword_테스트() throws Exception {

        // given
        String checkPassword = "12345";
        String updatePassword = "123456";

        // when
        userService.updatePassword(checkPassword, updatePassword);
        flushAndclear();

        User updateUser = userRepository.findTopByOrderById()
                .orElseThrow(() -> new Exception("생성된 user를 찾을 수 없습니다."));

        // then
        if (!updateUser.matchPassword(passwordEncoder, updatePassword)) {
            throw new Exception("비밀번호 변경이 제대로 되지 않았습니다.");
        }
    }

    @Test
    public void delete_테스트() throws Exception {

        // given
        signUp_테스트();

        String checkPassword = "12345";

        // when
        userService.delete(checkPassword);
        flushAndclear();

        User user = userRepository.findTopByExpiredAtIsNullOrderById()
                .orElse(null);

        // then
        assertThat(user).isNull();
    }

    @Test
    public void getMe_테스트() throws Exception {

        // given
        signUp_테스트();
        // TODO: 위 계정으로 로그인
        User user = userRepository.findTopByOrderById()
                .orElseThrow(() -> new Exception("생성된 user를 찾을 수 없습니다."));

        // when
        UserResponse userResponse = userService.getMe();

        // then
        assertThat(userResponse.getUsername()).isEqualTo(user.getUsername());
        // username 은 고유하므로 이것만 검증
    }
}
