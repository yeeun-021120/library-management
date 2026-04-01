package com.library.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.library.dto.BookStatisticsDTO;
import com.library.dto.CategoryStatisticsDTO;
import com.library.dto.MonthlyStatisticsDTO;

/**
 * 통계 Mapper
 */
@Mapper
public interface StatisticsMapper {

    /**
     * 월별 대여 통계
     */
    List<MonthlyStatisticsDTO> getMonthlyStatistics(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    /**
     * 인기 도서 TOP N
     */
    List<BookStatisticsDTO> getTopBooks(@Param("limit") int limit);

    /**
     * 카테고리별 통계
     */
    List<CategoryStatisticsDTO> getCategoryStatistics();
}