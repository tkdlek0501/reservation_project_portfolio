package com.portfolio.reservation.repository;

import com.portfolio.reservation.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByUsername(String username);

    Optional<User> findByRefreshToken(String refreshToke);

    Optional<User> findTopByOrderById();

    Optional<User> findTopByExpiredAtIsNullOrderById();
}
