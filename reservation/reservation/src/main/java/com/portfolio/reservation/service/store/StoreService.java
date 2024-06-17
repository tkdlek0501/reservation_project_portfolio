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
import com.portfolio.reservation.service.user.UserService;
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
    private final StoreRepositoryCustom storeRepositoryCustom;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void create(StoreCreateRequest request) {

        User user = userService.getUserByUsername();

        if (storeRepository.findByNameAndExpiredAtIsNull(request.getName()).isPresent()) {
            throw new AlreadyExistsStoreException();
        }

        Store store = StoreCreateRequest.toEntity(request, user.getId());

        storeRepository.save(store);
    }

    @Transactional
    public void update(StoreUpdateRequest request) {

        User user = userService.getUserByUsername();

        Store store = storeRepository.findByUserIdAndExpiredAtIsNull(user.getId())
                .orElseThrow(NotFoundStoreException::new);

        if (request.getName() != null) {
            store.updateName(request.getName());
        }
    }

    @Transactional
    public void delete(String checkPassword) {

        User user = userService.getUserByUsername();

        if (!user.matchPassword(passwordEncoder, checkPassword)) {
            throw new NotMatchedPasswordException();
        }

        Store store = storeRepository.findByUserIdAndExpiredAtIsNull(user.getId())
                .orElseThrow(NotFoundStoreException::new);

        store.expire();
    }

    public StoreResponse getMyStore() {

        User user = userService.getUserByUsername();

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

    public Store findById(Long id) {

        return storeRepository.findByIdAndExpiredAtIsNull(id)
                .orElseThrow(NotFoundStoreException::new);
    }
}
