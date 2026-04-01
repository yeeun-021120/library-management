package com.library.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.library.domain.User;

/**
 * 회원 Mapper
 */
@Mapper
public interface UserMapper {
    
    /**
     * 이메일로 회원 조회
     */
    User findByEmail(@Param("email") String email);
    
    /**
     * 회원 등록
     */
    int insert(User user);
    
    /**
     * 회원 정보 수정
     */
    int update(User user);
    
    // ========== 관리자용 메서드 ==========
    
    /**
     * 전체 회원 목록 조회 (검색, 필터)
     */
    List<User> findAllWithFilter(
        @Param("keyword") String keyword,
        @Param("status") String status,
        @Param("role") String role
    );
    
    /**
     * 회원 ID로 조회
     */
    User findById(@Param("userId") Long userId);
    
    /**
     * 회원 상태 변경
     */
    int updateStatus(
        @Param("userId") Long userId,
        @Param("status") String status,
        @Param("restrictUntil") String restrictUntil
    );
    
    /**
     * 전체 회원 수
     */
    int countAll();
    
    /**
     * 상태별 회원 수
     */
    int countByStatus(@Param("status") String status);
}