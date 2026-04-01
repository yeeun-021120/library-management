package com.library.domain;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 도서 실물 도메인
 * DB 테이블: book_copy
 */
@Data
public class BookCopy {
    
    private Long copyId;                 // copy_id
    private Long bookId;                 // book_id (FK)
    private String status;               // 상태 (AVAILABLE, RESERVED, RENTED, LOST)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}