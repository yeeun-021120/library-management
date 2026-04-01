package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 월별 통계 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStatisticsDTO {
    private String month;           // 연월 (YYYY-MM)
    private Integer totalRentals;   // 총 대여 건수
    private Integer totalReturns;   // 총 반납 건수
    private Integer overdueCount;   // 연체 건수
    private Integer newMembers;     // 신규 회원 수
    private Integer activeMembers;  // 활성 회원 수
}