package com.library.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.domain.User;
import com.library.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

/**
 * 회원 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 등록
     */
    @Transactional
    public void registerUser(User user) {
        // 이메일 중복 체크
        if (userMapper.findByEmail(user.getEmail()) != null) {
            throw new IllegalStateException("이미 사용중인 이메일입니다.");
        }

        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 기본 권한 설정
        if (user.getRole() == null) {
            user.setRole("USER");
        }

        userMapper.insert(user);
    }

    /**
     * 이메일로 회원 조회
     */
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    // ========== 관리자용 메서드 ==========

    /**
     * 전체 회원 목록 조회 (검색, 필터)
     */
    public List<User> getAllUsers(String keyword, String status, String role) {
        return userMapper.findAllWithFilter(keyword, status, role);
    }

    /**
     * 회원 상세 조회
     */
    public User getUserById(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new IllegalStateException("회원을 찾을 수 없습니다.");
        }
        return user;
    }

    /**
     * 회원 정지 (LocalDate 버전)
     */
    @Transactional
    public void suspendUser(Long userId, LocalDate restrictUntil) {
        userMapper.updateStatus(userId, "SUSPENDED", restrictUntil.toString());
    }

    /**
     * 회원 정지 (일수 버전 - 기존 호환성 유지)
     */
    @Transactional
    public void suspendUser(Long userId, int days) {
        LocalDate restrictUntil = LocalDate.now().plusDays(days);
        userMapper.updateStatus(userId, "SUSPENDED", restrictUntil.toString());
    }

    /**
     * 회원 정지 해제
     */
    @Transactional
    public void activateUser(Long userId) {
        userMapper.updateStatus(userId, "ACTIVE", null);
    }

    /**
     * 회원 탈퇴 처리
     */
    @Transactional
    public void withdrawUser(Long userId) {
        userMapper.updateStatus(userId, "WITHDRAWN", null);
    }

    /**
     * 회원 통계
     */
    public int getTotalUsers() {
        return userMapper.countAll();
    }

    public int getActiveUsers() {
        return userMapper.countByStatus("ACTIVE");
    }

    public int getSuspendedUsers() {
        return userMapper.countByStatus("SUSPENDED");
    }
    
    /**
     * 이메일로 사용자 조회
     */
    public User getUserByEmail(String email) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new IllegalStateException("사용자를 찾을 수 없습니다.");
        }
        return user;
    }
}