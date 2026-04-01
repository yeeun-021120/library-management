package com.library.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 도서 도메인
 * DB 테이블: book
 */
@Data
public class Book {
    
    private Long bookId;                 // book_id
    private String title;                // 도서명
    private String author;               // 저자
    private String publisher;            // 출판사
    private String isbn;                 // ISBN
    private Integer categoryId;          // 카테고리 ID (FK)
    private String description;          // 설명
    private String coverImage;        // 표지 이미지 경로
    private LocalDate publishDate;       // 출판일
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // JOIN 결과용 (DTO 역할)
    private String categoryName;         // categories 테이블에서 조인
    private Integer totalCopies;         // 총 실물 수
    private Integer availableCopies;     // 대여 가능 수
}