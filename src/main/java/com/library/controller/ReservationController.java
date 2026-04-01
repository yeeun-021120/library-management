package com.library.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.domain.Reservation;
import com.library.service.ReservationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 예약 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 내 예약 목록
     */
    @GetMapping("/my")
    public String myReservations(Principal principal, Model model) {
        String email = principal.getName();
        List<Reservation> reservations = reservationService.getUserReservations(email);

        // 총 개수 계산
        long activeCount = reservations.stream()
                .filter(r -> "ACTIVE".equals(r.getReservationStatus()))
                .count();

        model.addAttribute("reservations", reservations);
        model.addAttribute("activeCount", activeCount);

        return "reservation/my";
    }

    /**
     * 도서 예약
     */
    @PostMapping("/reserve/{bookId}")
    public String reserve(
            @PathVariable Long bookId,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            String email = principal.getName();
            reservationService.reserveBook(bookId, email);
            redirectAttributes.addFlashAttribute("message", "예약이 완료되었습니다.");
        } catch (Exception e) {
            log.error("예약 실패", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/reservation/my";
    }

    /**
     * 예약 취소 ⭐ 추가
     */
    @PostMapping("/{reservationId}/cancel")
    public String cancel(
            @PathVariable Long reservationId,
            RedirectAttributes redirectAttributes) {

        try {
            reservationService.cancelReservation(reservationId);
            redirectAttributes.addFlashAttribute("message", "예약이 취소되었습니다.");
        } catch (Exception e) {
            log.error("예약 취소 실패", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/reservation/my";
    }
}