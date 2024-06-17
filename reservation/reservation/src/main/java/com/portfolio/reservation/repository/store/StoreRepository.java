package com.portfolio.reservation.repository.store;

import com.portfolio.reservation.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByNameAndExpiredAtIsNull(String name);

    Optional<Store> findByUserIdAndExpiredAtIsNull(Long userId);

    Optional<Store> findTopByOrderById();

    Optional<Store> findByIdAndExpiredAtIsNull(Long id);
}
