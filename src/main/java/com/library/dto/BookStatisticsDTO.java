package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 도서 통계 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookStatisticsDTO {
    private Long bookId;
    private String title;
    private String author;
    private String categoryName;
    private Integer rentalCount;
    private Integer currentAvailable;
}