package com.library.domain;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 예약 도메인
 */
@Data
public class Reservation {
    
    private Long reservationId;      // 예약 ID
    private Long userId;              // 회원 ID
    private Long bookId;              // 도서 ID
    private String reservationStatus; // 예약 상태 (ACTIVE, COMPLETED, CANCELLED, EXPIRED)
    private LocalDateTime reservedAt; // 예약 일시
    private LocalDateTime expiresAt;  // 만료 일시
    private LocalDateTime completedAt; // 완료 일시 (대여로 전환된 시간)
    private LocalDateTime cancelledAt; // 취소 일시
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // JOIN용 필드
    private String userName;          // 회원명
    private String bookTitle;         // 도서명
}