package com.library.domain;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 카테고리 도메인
 * DB 테이블: categories
 */
@Data
public class Category {
    
    private Integer categoryId;          // category_id
    private String categoryName;         // 카테고리명
    private String description;          // 설명
    private Integer displayOrder;        // 표시 순서
    private Boolean isActive;            // 활성화 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}