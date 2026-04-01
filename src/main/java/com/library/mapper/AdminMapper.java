package com.library.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.library.domain.Rental;
import com.library.domain.User;

/**
 * 관리자 Mapper 인터페이스
 */
@Mapper
public interface AdminMapper {
    
    /**
     * 전체 도서 수
     */
    long countTotalBooks();
    
    /**
     * 전체 회원 수
     */
    long countTotalUsers();
    
    /**
     * 현재 대여중인 도서 수
     */
    long countActiveRentals();
    
    /**
     * 연체 도서 수
     */
    long countOverdueRentals();
    
    /**
     * 최근 대여 내역
     */
    List<Rental> findRecentRentals(@Param("limit") int limit);
    
    /**
     * 전체 회원 목록
     */
    List<User> findAllUsers();
    
    /**
     * 회원 정보 수정
     */
    int updateUser(User user);
    
    /**
     * 전체 대여 내역
     */
    List<Rental> findAllRentals();
}