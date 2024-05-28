package com.portfolio.reservation.service.store;

import com.portfolio.reservation.domain.store.Store;
import com.portfolio.reservation.domain.user.User;
import com.portfolio.reservation.dto.store.StoreCreateRequest;
import com.portfolio.reservation.dto.store.StoreResponse;
import com.portfolio.reservation.dto.store.StoreUpdateRequest;
import com.portfolio.reservation.dto.store.StoreWithUserDto;
import com.portfolio.reservation.exception.store.AlreadyExistsStoreException;
import com.portfolio.reservation.exception.store.NotFoundStoreException;
import com.portfolio.reservation.exception.user.NotFoundUserException;
import com.portfolio.reservation.exception.user.NotLoginUserException;
import com.portfolio.reservation.exception.user.NotMatchedPasswordException;
import com.portfolio.reservation.repository.store.StoreRepository;
import com.portfolio.reservation.repository.store.StoreRepositoryCustom;
import com.portfolio.reservation.repository.user.UserRepository;
import com.portfolio.reservation.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    private final UserRepository userRepository;

    private final StoreRepositoryCustom storeRepositoryCustom;

    private final PasswordEncoder passwordEncoder;

    // TODO: 서비스간 계층 구조 따르기
    // store 는 하위 서비스
//    하위 서비스: 특정 도메인이나 기능에 대해 명확한 책임을 가지고 독립적으로 동작합니다. 예를 들어, 결제 처리, 재고 관리, 사용자 인증 등 특정 기능을 수행하는 서비스입니다.
//    상위 서비스: 여러 하위 서비스를 조합하여 더 복잡한 비즈니스 로직을 처리합니다. 상위 서비스는 비즈니스 프로세스나 워크플로우를 관리하고 조정합니다.

    @Transactional
    public void create(StoreCreateRequest request) {

        User user = userRepository.findOneByUsernameAndExpiredAtIsNull(SecurityUtil.getLoginUsername())
                .orElseThrow(NotLoginUserException::new);

        if (storeRepository.findByNameAndExpiredAtIsNull(request.getName()).isPresent()) {
            throw new AlreadyExistsStoreException();
        }

        Store store = StoreCreateRequest.toEntity(request, user.getId());

        storeRepository.save(store);
    }

    @Transactional
    public void update(StoreUpdateRequest request) {

        User user = userRepository.findOneByUsernameAndExpiredAtIsNull(SecurityUtil.getLoginUsername())
                .orElseThrow(NotLoginUserException::new);

        Store store = storeRepository.findByUserIdAndExpiredAtIsNull(user.getId())
                .orElseThrow(NotFoundStoreException::new);

        if (request.getName() != null) {
            store.updateName(request.getName());
        }
    }

    @Transactional
    public void delete(String checkPassword) {

        User user = userRepository.findOneByUsernameAndExpiredAtIsNull(SecurityUtil.getLoginUsername())
                .orElseThrow(NotLoginUserException::new);

        if (!user.matchPassword(passwordEncoder, checkPassword)) {
            throw new NotMatchedPasswordException();
        }

        Store store = storeRepository.findByUserIdAndExpiredAtIsNull(user.getId())
                .orElseThrow(NotFoundStoreException::new);

        store.expire();
    }

    public StoreResponse getMyStore() {

        User user = userRepository.findOneByUsernameAndExpiredAtIsNull(SecurityUtil.getLoginUsername())
                .orElseThrow(NotLoginUserException::new);

        Store store = storeRepository.findByUserIdAndExpiredAtIsNull(user.getId())
                .orElseThrow(NotFoundStoreException::new);

        return StoreResponse.of(store.getId(), store.getName(), user.getNickname());
    }

    public StoreResponse getStore(Long id) {

        StoreWithUserDto dto = storeRepositoryCustom.findStoreWithUser(id);

        return StoreResponse.of(dto.getStoreId(), dto.getStoreName(), dto.getNickName());
    }

    public Store findByUserId(Long userId) {

        return storeRepository.findByUserIdAndExpiredAtIsNull(userId)
                .orElseThrow(NotFoundStoreException::new);
    }
}
