package com.library.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 대여 도메인
 */
@Data
public class Rental {
    private Long rentalId;
    private Long userId;
    private Long copyId;
    private Long bookId; // JOIN 필드
    private String rentalStatus; // RENTED, RETURNED
    private LocalDateTime reservedAt;
    private LocalDate rentalDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Integer overdueDays;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // JOIN 필드
    private String userName;
    private String bookTitle;
}