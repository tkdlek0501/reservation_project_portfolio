package com.portfolio.reservation.service.user;

import com.portfolio.reservation.domain.user.AuthorityType;
import com.portfolio.reservation.domain.user.User;
import com.portfolio.reservation.dto.user.UserCreateRequest;
import com.portfolio.reservation.dto.user.UserResponse;
import com.portfolio.reservation.dto.user.UserUpdateRequest;
import com.portfolio.reservation.exception.user.AlreadyExistsUserException;
import com.portfolio.reservation.exception.user.NotFoundUserException;
import com.portfolio.reservation.exception.user.NotLoginUserException;
import com.portfolio.reservation.exception.user.NotMatchedPasswordException;
import com.portfolio.reservation.repository.user.UserRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        flushAndclear();
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
    public void singUp_AlreadyExistsUserException_테스트() throws Exception {

        // given
        setUser();

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

        // when & then
        assertThrows(AlreadyExistsUserException.class, () -> {
            userService.signUp(request);
        });

        List<User> users = userRepository.findAll();
        assertThat(users.size()).isEqualTo(1);
    }

    // 통과
    @Test
    public void getUser_테스트() throws Exception {

        // given
        User user = setUser();

        // when
        UserResponse userResponse = userService.getUser(user.getId());

        // then
        assertThat(userResponse).isNotNull();
    }

    @Test
    public void getUser_NotFoundUserException_테스트() throws Exception {

        // given
        setUser();

        // when & then
        assertThrows(NotFoundUserException.class, () -> {
            userService.getUser(0L);
        });
    }

    // 통과
    @Test
    public void update_테스트() throws Exception {

        // given
        setUser();

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

    // 통과
    @Test
    public void update_NotLoginUserException_테스트() {

        // given
        UserUpdateRequest request = new UserUpdateRequest(
                "nickname2"
        );

        // when & then
        assertThrows(NotLoginUserException.class, () -> {
            userService.update(request);
        });
    }

    // 통과
    @Test
    public void updatePassword_테스트() throws Exception {

        // given
        setUser();

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

    // 통과
    @Test
    public void updatePassword_NotLoginUserException_테스트() {

        // given
        String checkPassword = "12345";
        String updatePassword = "123456";

        // when & then
        assertThrows(NotLoginUserException.class, () -> {
            userService.updatePassword(checkPassword, updatePassword);
        });
    }

    // 통과
    @Test
    public void updatePassword_NotMatchedPassword_테스트() throws Exception {

        // given
        setUser();

        // when
        String checkPassword = "123456"; // 기존과 다른 패스워드
        String updatePassword = "123456";

        // then
        assertThrows(NotMatchedPasswordException.class, () -> {
            userService.updatePassword(checkPassword, updatePassword);
        });
    }

    // 통과
    @Test
    public void delete_테스트() throws Exception {

        // given
        setUser();

        String checkPassword = "12345";

        // when
        userService.delete(checkPassword);
        flushAndclear();

        User user = userRepository.findTopByExpiredAtIsNullOrderById()
                .orElse(null);

        // then
        assertThat(user).isNull();
    }

    // 통과
    @Test
    public void delete_NotLoginUserException_테스트() {

        // given
        String checkPassword = "12345";

        // when & then
        assertThrows(NotLoginUserException.class, () -> {
            userService.delete(checkPassword);
        });
    }

    // 통과
    @Test
    public void delete_NotMatchedPassword_테스트() throws Exception {

        // given
        setUser();
        String checkPassword = "123456"; // 틀린 비밀번호

        // when & then
        assertThrows(NotMatchedPasswordException.class, () -> {
            userService.delete(checkPassword);
        });
    }

    // 통과
    @Test
    public void getMe_테스트() throws Exception {

        // given
        setUser();

        User user = userRepository.findTopByOrderById()
                .orElseThrow(() -> new Exception("생성된 user를 찾을 수 없습니다."));

        // when
        UserResponse userResponse = userService.getMe();

        // then
        assertThat(userResponse.getUsername()).isEqualTo(user.getUsername());
        // username 은 고유하므로 이것만 검증
    }

    // 통과
    @Test
    public void getMe_NotLoginUserException_테스트() {

        // given
        // when & then
        assertThrows(NotLoginUserException.class, () -> {
            userService.getMe();
        });
    }
}
