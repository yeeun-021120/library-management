package com.library.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.library.dto.BookStatisticsDTO;
import com.library.dto.CategoryStatisticsDTO;
import com.library.dto.MonthlyStatisticsDTO;
import com.library.service.StatisticsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 통계 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/admin/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * 통계 대시보드
     */
    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            Model model) {

        // 기본값: 현재 연월
        YearMonth targetMonth = (year != null && month != null)
            ? YearMonth.of(year, month)
            : YearMonth.now();

        // 월별 대여 통계 (최근 12개월)
        List<MonthlyStatisticsDTO> monthlyStats = statisticsService.getMonthlyStatistics(12);

        // 인기 도서 TOP 10
        List<BookStatisticsDTO> popularBooks = statisticsService.getTopBooks(10);

        // 카테고리별 통계
        List<CategoryStatisticsDTO> categoryStats = statisticsService.getCategoryStatistics();

        model.addAttribute("targetMonth", targetMonth);
        model.addAttribute("monthlyStats", monthlyStats);
        model.addAttribute("popularBooks", popularBooks);
        model.addAttribute("categoryStats", categoryStats);

        return "admin/statistics/dashboard";
    }

    /**
     * 월별 통계 데이터 (JSON)
     */
    @GetMapping("/monthly-data")
    @ResponseBody
    public Map<String, Object> getMonthlyData(@RequestParam(defaultValue = "12") int months) {
        List<MonthlyStatisticsDTO> stats = statisticsService.getMonthlyStatistics(months);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", stats);

        return result;
    }

    /**
     * 인기 도서 데이터 (JSON)
     */
    @GetMapping("/top-books-data")
    @ResponseBody
    public Map<String, Object> getTopBooksData(@RequestParam(defaultValue = "10") int limit) {
        List<BookStatisticsDTO> stats = statisticsService.getTopBooks(limit);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", stats);

        return result;
    }

    /**
     * 카테고리 통계 데이터 (JSON)
     */
    @GetMapping("/category-data")
    @ResponseBody
    public Map<String, Object> getCategoryData() {
        List<CategoryStatisticsDTO> stats = statisticsService.getCategoryStatistics();

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", stats);

        return result;
    }
}