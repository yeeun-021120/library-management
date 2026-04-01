package com.library.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.dto.BookStatisticsDTO;
import com.library.dto.CategoryStatisticsDTO;
import com.library.dto.MonthlyStatisticsDTO;
import com.library.mapper.StatisticsMapper;

import lombok.RequiredArgsConstructor;

/**
 * 통계 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final StatisticsMapper statisticsMapper;

    /**
     * 월별 대여 통계 (최근 N개월)
     */
    public List<MonthlyStatisticsDTO> getMonthlyStatistics(int months) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(months - 1).withDayOfMonth(1);
        return statisticsMapper.getMonthlyStatistics(startDate, endDate);
    }

    /**
     * 인기 도서 TOP N
     */
    public List<BookStatisticsDTO> getTopBooks(int limit) {
        return statisticsMapper.getTopBooks(limit);
    }

    /**
     * 카테고리별 통계
     */
    public List<CategoryStatisticsDTO> getCategoryStatistics() {
        return statisticsMapper.getCategoryStatistics();
    }
}