package com.library.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.domain.Reservation;
import com.library.mapper.BookCopyMapper;
import com.library.mapper.ReservationMapper;
import com.library.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 예약 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationMapper reservationMapper;
    private final UserMapper userMapper;
    private final BookCopyMapper bookCopyMapper;

    // ========== 일반 사용자 메서드 ==========

    /**
     * 사용자의 예약 목록 조회
     */
    public List<Reservation> getUserReservations(String email) {
        Long userId = userMapper.findByEmail(email).getUserId();
        return reservationMapper.findByUserId(userId);
    }

    /**
     * 도서 예약
     */
    @Transactional
    public void reserveBook(Long bookId, String email) {
        Long userId = userMapper.findByEmail(email).getUserId();
        
        log.info("=== 예약 시작: userId={}, bookId={} ===", userId, bookId);
        
        // 1. 중복 예약 확인
        int existingCount = reservationMapper.countActiveByUserAndBook(userId, bookId);
        if (existingCount > 0) {
            throw new IllegalStateException("이미 예약한 도서입니다.");
        }
        
        // 2. 예약 등록
        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setBookId(bookId);
        reservation.setReservationStatus("ACTIVE");
        
        reservationMapper.insert(reservation);
        
        log.info("예약 완료: reservationId={}", reservation.getReservationId());
    }

    /**
     * 예약 취소
     */
    @Transactional
    public void cancelReservation(Long reservationId) {
        log.info("=== 예약 취소: reservationId={} ===", reservationId);
        
        Reservation reservation = reservationMapper.findById(reservationId);
        if (reservation == null) {
            throw new IllegalStateException("예약 정보를 찾을 수 없습니다.");
        }
        
        if (!"ACTIVE".equals(reservation.getReservationStatus())) {
            throw new IllegalStateException("이미 처리된 예약입니다.");
        }
        
        reservationMapper.cancel(reservationId);
        log.info("예약 취소 완료");
    }

    /**
     * 사용자의 활성 예약 여부 확인 (BookController용)
     */
    public boolean hasActiveReservation(String email, Long bookId) {
        Long userId = userMapper.findByEmail(email).getUserId();
        int count = reservationMapper.countActiveByUserAndBook(userId, bookId);
        return count > 0;
    }

    /**
     * 사용자의 대여 가능한 예약 목록 조회 (GlobalControllerAdvice용)
     */
    public List<Reservation> getReadyReservations(String email) {
        Long userId = userMapper.findByEmail(email).getUserId();
        return reservationMapper.findReadyReservations(userId);
    }

    // ========== 스케줄러용 메서드 ==========

    /**
     * 만료된 예약 처리 (스케줄러)
     */
    @Transactional
    public void expireReservations() {
        log.info("=== 만료된 예약 처리 시작 ===");
        
        List<Reservation> expiredList = reservationMapper.findExpiredReservations();
        
        for (Reservation reservation : expiredList) {
            try {
                reservationMapper.expireReservation(reservation.getReservationId());
                log.info("예약 만료 처리: reservationId={}", reservation.getReservationId());
            } catch (Exception e) {
                log.error("예약 만료 처리 실패: reservationId={}", reservation.getReservationId(), e);
            }
        }
        
        log.info("=== 만료된 예약 처리 완료: {}건 ===", expiredList.size());
    }

    // ========== 관리자용 메서드 ==========

    /**
     * 전체 예약 목록 (관리자)
     */
    public List<Reservation> getAllReservations() {
        return reservationMapper.findAll();
    }

    /**
     * 상태별 예약 목록 (관리자)
     */
    public List<Reservation> getReservationsByStatus(String status) {
        return reservationMapper.findByStatus(status);
    }

    /**
     * 검색 (관리자)
     */
    public List<Reservation> searchReservations(String keyword) {
        return reservationMapper.searchReservations(keyword);
    }

    /**
     * 관리자 예약 취소
     */
    @Transactional
    public void adminCancelReservation(Long reservationId) {
        // 기존 취소 로직 재사용
        cancelReservation(reservationId);
    }

    /**
     * 통계 - 활성 예약 수
     */
    public int getActiveReservationsCount() {
        return reservationMapper.countByStatus("ACTIVE");
    }

    /**
     * 통계 - 완료된 예약 수
     */
    public int getCompletedReservationsCount() {
        return reservationMapper.countByStatus("COMPLETED");
    }

    /**
     * 통계 - 취소된 예약 수
     */
    public int getCancelledReservationsCount() {
        return reservationMapper.countByStatus("CANCELLED");
    }

    /**
     * 통계 - 만료된 예약 수
     */
    public int getExpiredReservationsCount() {
        return reservationMapper.countByStatus("EXPIRED");
    }

    /**
     * 통계 - 전체 예약 수
     */
    public int getTotalReservationsCount() {
        return reservationMapper.countAll();
    }
}