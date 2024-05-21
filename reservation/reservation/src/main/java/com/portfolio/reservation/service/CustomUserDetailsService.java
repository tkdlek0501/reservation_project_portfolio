package com.portfolio.reservation.service;


import com.portfolio.reservation.auth.CustomUserPrincipal;
import com.portfolio.reservation.domain.user.User;
import com.portfolio.reservation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {

        return CustomUserPrincipal.of(userRepository.findOneByUsernameAndExpiredAtIsNull(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다.")));
    }

    public UserDetails loadUserByUserId(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("데이터베이스에서 찾을 수 없습니다."));

        return CustomUserPrincipal.of(user);
    }

    private org.springframework.security.core.userdetails.User createUser(User user) {

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getAuthority().name()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                grantedAuthorities);
    }
}

