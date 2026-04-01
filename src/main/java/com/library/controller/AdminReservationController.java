package com.library.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.domain.Reservation;
import com.library.service.ReservationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 예약 관리 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/admin/reservation")
@RequiredArgsConstructor
public class AdminReservationController {

    private final ReservationService reservationService;

    /**
     * 예약 목록 페이지 (전체, 검색, 필터)
     */
    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            Model model) {
        
        List<Reservation> reservations;
        
        // 검색 또는 필터링
        if (keyword != null && !keyword.trim().isEmpty()) {
            reservations = reservationService.searchReservations(keyword);
        } else if (status != null && !status.trim().isEmpty()) {
            reservations = reservationService.getReservationsByStatus(status);
        } else {
            reservations = reservationService.getAllReservations();
        }
        
        // 통계
        int activeCount = reservationService.getActiveReservationsCount();
        int completedCount = reservationService.getCompletedReservationsCount();
        int cancelledCount = reservationService.getCancelledReservationsCount();
        int expiredCount = reservationService.getExpiredReservationsCount();
        
        model.addAttribute("reservations", reservations);
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("cancelledCount", cancelledCount);
        model.addAttribute("expiredCount", expiredCount);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        
        return "admin/reservation/list";
    }

    /**
     * 관리자 예약 취소
     */
    @PostMapping("/cancel/{reservationId}")
    public String cancelReservation(
            @PathVariable Long reservationId,
            RedirectAttributes redirectAttributes) {
        
        try {
            reservationService.adminCancelReservation(reservationId);
            redirectAttributes.addFlashAttribute("message", "예약이 취소되었습니다.");
        } catch (Exception e) {
            log.error("예약 취소 실패", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin/reservation/list";
    }
}