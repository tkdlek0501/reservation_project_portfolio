package com.portfolio.reservation.service;

import com.portfolio.reservation.domain.user.User;
import com.portfolio.reservation.dto.user.UserCreateRequest;
import com.portfolio.reservation.dto.user.UserResponse;
import com.portfolio.reservation.dto.user.UserUpdateRequest;
import com.portfolio.reservation.exception.AlreadyExistsUserException;
import com.portfolio.reservation.exception.NotFoundUserException;
import com.portfolio.reservation.exception.NotLoginUserException;
import com.portfolio.reservation.exception.NotMatchedPasswordException;
import com.portfolio.reservation.repository.UserRepository;
import com.portfolio.reservation.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(UserCreateRequest request) {

        User user = UserCreateRequest.toEntity(request, passwordEncoder);

        if (userRepository.findOneByUsername(request.getUsername()).isPresent()) {
            throw new AlreadyExistsUserException();
        }

        userRepository.save(user);
    }

    @Transactional
    public void update(UserUpdateRequest request) {

        User user = userRepository.findOneByUsername(SecurityUtil.getLoginUsername())
                .orElseThrow(NotLoginUserException::new);

        request.getNickname().ifPresent(user::updateNickname);
        // TODO: 나중에 필드가 많아지다면 builder 로 처리
    }

    @Transactional
    public void updatePassword(String checkPassword, String updatePassword) {

        User user = userRepository.findOneByUsername(SecurityUtil.getLoginUsername())
                .orElseThrow(NotLoginUserException::new);

        if (!user.matchPassword(passwordEncoder, checkPassword)) {
            throw new NotMatchedPasswordException();
        }

        user.updatePassword(passwordEncoder, updatePassword);
    }

    @Transactional
    public void delete(String checkPassword) {

        User user = userRepository.findOneByUsername(SecurityUtil.getLoginUsername())
                .orElseThrow(NotLoginUserException::new);

        if (!user.matchPassword(passwordEncoder, checkPassword)) {
            throw new NotMatchedPasswordException();
        }

        user.expire();
        // TODO: 데이터의 삭제는 delte 가 아닌 expire 로 처리할 것이기 때문에
        // 유효한 데이터는 expiredAtIsNull 조건을 주도록 수정이 필요
    }

    public UserResponse getUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(NotFoundUserException::new);

        return UserResponse.of(user);
    }

    public UserResponse getMe() {

        User user = userRepository.findOneByUsername(SecurityUtil.getLoginUsername())
                .orElseThrow(NotLoginUserException::new);

        return UserResponse.of(user);
    }
}
