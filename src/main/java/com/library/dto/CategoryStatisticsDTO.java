package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 카테고리 통계 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryStatisticsDTO {
    private Long categoryId;
    private String categoryName;
    private Integer bookCount;
    private Integer rentalCount;
    private Double rentalRate;
}