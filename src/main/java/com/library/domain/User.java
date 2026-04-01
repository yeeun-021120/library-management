package com.library.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 회원 도메인
 */
@Data
public class User {
    private Long userId;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String role;              // USER, ADMIN
    private String status;            // ACTIVE, SUSPENDED, WITHDRAWN
    private LocalDate restrictUntil;  // 정지 기간
    private String preferredLocation; // 선호 수령 장소
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 통계용 필드 (JOIN)
    private Integer activeRentals;    // 활성 대여 수
    private Integer totalRentals;     // 총 대여 수
    private Integer overdueCount;     // 연체 횟수
}