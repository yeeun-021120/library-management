package com.library.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.library.domain.Rental;

/**
 * 대여 Mapper
 */
@Mapper
public interface RentalMapper {
    
    // ========== 기본 CRUD ==========
    
    /**
     * 대여 등록
     */
    int insert(Rental rental);
    
    /**
     * 대여 ID로 조회
     */
    Rental findById(@Param("rentalId") Long rentalId);
    
    /**
     * 사용자 ID로 대여 목록 조회
     */
    List<Rental> findByUserId(@Param("userId") Long userId);
    
    /**
     * 반납 처리
     */
    int updateReturn(
        @Param("rentalId") Long rentalId,
        @Param("returnDate") LocalDate returnDate
    );
    
    // ========== 검증 및 카운트 ==========
    
    /**
     * 사용자의 활성 대여 수
     */
    int countActiveRentalByUser(@Param("userId") Long userId);
    
    /**
     * 사용자가 특정 도서를 대여중인지 확인
     */
    int countActiveRentalByUserAndBook(
        @Param("userId") Long userId,
        @Param("bookId") Long bookId
    );
    
    /**
     * 대여 가능한 복본 찾기 (AVAILABLE)
     */
    Long findAvailableCopyId(@Param("bookId") Long bookId);
    
    /**
     * 예약된 복본 찾기 (RESERVED)
     */
    Long findReservedCopyId(@Param("bookId") Long bookId);
    
    /**
     * 최근 대여 목록
     */
    List<Rental> findRecent(@Param("limit") int limit);
    
    /**
     * 연체 대여 목록
     */
    List<Rental> findOverdueRentals();
    
    // ========== 관리자용 메서드 ==========
    
    /**
     * 전체 대여 목록
     */
    List<Rental> findAll();
    
    /**
     * 상태별 대여 목록
     */
    List<Rental> findByStatus(@Param("status") String status);
    
    /**
     * 검색 (회원명, 도서명)
     */
    List<Rental> searchRentals(@Param("keyword") String keyword);
    
    /**
     * 상태별 대여 수
     */
    int countByStatus(@Param("status") String status);
    
    /**
     * 전체 대여 수
     */
    int countAll();
}