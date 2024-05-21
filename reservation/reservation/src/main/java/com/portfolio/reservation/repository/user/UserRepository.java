package com.portfolio.reservation.repository.user;

import com.portfolio.reservation.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByUsernameAndExpiredAtIsNull(String username);

    Optional<User> findByRefreshTokenAndExpiredAtIsNull(String refreshToken);

    Optional<User> findTopByOrderById();

    Optional<User> findTopByExpiredAtIsNullOrderById();
}
