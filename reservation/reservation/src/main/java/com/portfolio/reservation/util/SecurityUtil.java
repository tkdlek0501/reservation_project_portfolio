package com.portfolio.reservation.util;

import com.portfolio.reservation.domain.user.User;
import com.portfolio.reservation.exception.user.NotLoginUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    public static String getLoginUsername() {
        try {
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            return user.getUsername();
        } catch (Exception e) {
            log.info("로그인 정보 가져오기 실패 : {}", e.getMessage());
            throw new NotLoginUserException();
        }
    }
}
