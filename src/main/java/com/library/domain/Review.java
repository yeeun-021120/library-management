package com.library.domain;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 후기 도메인
 * DB 테이블: reviews
 */
@Data
public class Review {
    
    private Long reviewId;               // review_id
    private Long userId;                 // user_id (FK)
    private Long bookId;                 // book_id (FK)
    private String content;              // 후기 내용
    private Integer rating;              // 평점 (1~5)
    private Integer likeCount;           // 좋아요 수
    private LocalDateTime deletedAt;     // 삭제 일시 (Soft Delete)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // JOIN 결과용
    private String userName;             // users.name
    private String bookTitle;            // book.title
}