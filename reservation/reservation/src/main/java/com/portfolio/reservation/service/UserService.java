package com.portfolio.reservation.service;

import com.portfolio.reservation.domain.user.User;
import com.portfolio.reservation.dto.user.UserCreateRequest;
import com.portfolio.reservation.dto.user.UserResponse;
import com.portfolio.reservation.dto.user.UserUpdateRequest;
import com.portfolio.reservation.repository.UserRepository;
import com.portfolio.reservation.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(UserCreateRequest request) throws Exception {

        User user = request.toEntity(request, passwordEncoder);

        if (userRepository.findOneByUsername(request.getUsername()).isPresent()) {
            throw new Exception("이미 해당 아이디의 회원이 있습니다.");
            // TODO: Exception 처리는 서비스 작업 끝나면 나중에 한 번에 하기!
        }

        userRepository.save(user);
    }

    @Transactional
    public void update(UserUpdateRequest request) throws Exception {

        User user = userRepository.findOneByUsername(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new Exception("로그인 정보가 없습니다. 정보 수정이 불가능 합니다."));

        request.getNickname().ifPresent(user::updateNickname);
        // TODO: 나중에 필드가 많아지다면 builder 로 처리
    }

    @Transactional
    public void updatePassword(String checkPassword, String updatePassword) throws Exception {

        User user = userRepository.findOneByUsername(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new Exception("로그인 정보가 없습니다. 정보 수정이 불가능 합니다."));

        if (!user.matchPassword(passwordEncoder, checkPassword)) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        user.updatePassword(passwordEncoder, updatePassword);
    }

    public void delete(String checkPassword) throws Exception {

        User user = userRepository.findOneByUsername(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new Exception("로그인 정보가 없습니다. 정보 수정이 불가능 합니다."));

        if (!user.matchPassword(passwordEncoder, checkPassword)) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        user.expire();
        // TODO: 데이터의 삭제는 delte 가 아닌 expire 로 처리할 것이기 때문에
        // 유효한 데이터는 expiredAtIsNull 조건을 주도록 수정이 필요
    }

    public UserResponse getUser(Long id) throws Exception {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("회원이 조회되지 않습니다."));

        return UserResponse.of(user);
    }

    public UserResponse getMe(Long id) throws Exception {

        User user = userRepository.findOneByUsername(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new Exception("로그인 정보가 없습니다."));

        return UserResponse.of(user);
    }
}
