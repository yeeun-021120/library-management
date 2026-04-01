package com.library.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.library.domain.Reservation;

/**
 * 예약 Mapper
 */
@Mapper
public interface ReservationMapper {
    
    // ========== 기본 CRUD ==========
    
    /**
     * 예약 등록
     */
    int insert(Reservation reservation);
    
    /**
     * 예약 ID로 조회
     */
    Reservation findById(@Param("reservationId") Long reservationId);
    
    /**
     * 사용자 ID로 예약 목록 조회
     */
    List<Reservation> findByUserId(@Param("userId") Long userId);
    
    /**
     * 예약 취소
     */
    int cancel(@Param("reservationId") Long reservationId);
    
    /**
     * 예약 완료 처리
     */
    int complete(@Param("reservationId") Long reservationId);
    
    // ========== 검증 및 조회 ==========
    
    /**
     * 특정 도서의 활성 예약 목록
     */
    List<Reservation> findActiveByBookId(@Param("bookId") Long bookId);
    
    /**
     * 사용자의 특정 도서 활성 예약 수
     */
    int countActiveByUserAndBook(
        @Param("userId") Long userId,
        @Param("bookId") Long bookId
    );
    
    /**
     * 사용자의 특정 도서 활성 예약 목록
     */
    List<Reservation> findActiveByUserAndBook(
        @Param("userId") Long userId,
        @Param("bookId") Long bookId
    );
    
    /**
     * 만료된 예약 목록 조회
     */
    List<Reservation> findExpiredReservations();
    
    /**
     * 예약 만료 처리
     */
    int expireReservation(@Param("reservationId") Long reservationId);
    
    /**
     * 예약에 만료 시간 설정
     */
    int setExpiryTime(@Param("reservationId") Long reservationId);
    
    /**
     * 사용자의 대여 가능한 예약 목록
     */
    List<Reservation> findReadyReservations(@Param("userId") Long userId);
    
    /**
     * 특정 도서의 활성 예약 수
     */
    int countActiveByBook(@Param("bookId") Long bookId);
    
    // ========== 관리자용 메서드 ==========
    
    /**
     * 전체 예약 목록
     */
    List<Reservation> findAll();
    
    /**
     * 상태별 예약 목록
     */
    List<Reservation> findByStatus(@Param("status") String status);
    
    /**
     * 검색 (회원명, 도서명)
     */
    List<Reservation> searchReservations(@Param("keyword") String keyword);
    
    /**
     * 상태별 예약 수
     */
    int countByStatus(@Param("status") String status);
    
    /**
     * 전체 예약 수
     */
    int countAll();
}