package com.library.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.library.domain.User;
import com.library.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

/**
 * Spring Security UserDetailsService 구현
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userMapper.findByEmail(email);
        
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email);
        }
        
        // 정지된 회원 체크
        if ("SUSPENDED".equals(user.getStatus())) {
            if (user.getRestrictUntil() != null && 
                user.getRestrictUntil().isAfter(LocalDate.now())) {
                throw new RuntimeException("정지된 계정입니다. 정지 기간: ~ " + user.getRestrictUntil());
            }
        }
        
        // 탈퇴한 회원 체크
        if ("WITHDRAWN".equals(user.getStatus())) {
            throw new RuntimeException("탈퇴한 계정입니다.");
        }
        
        // 권한 설정
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}