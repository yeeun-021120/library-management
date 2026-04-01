package com.library.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.library.domain.Reservation;
import com.library.service.ReservationService;

import lombok.RequiredArgsConstructor;

/**
 * 공통 컨트롤러 어드바이스
 * 모든 컨트롤러에 공통으로 적용될 데이터 추가
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final ReservationService reservationService;

    /**
     * 모든 페이지에 예약 알림 데이터 추가
     */
    @ModelAttribute
    public void addReservationAlerts(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            List<Reservation> readyReservations = reservationService.getReadyReservations(email);
            model.addAttribute("readyReservations", readyReservations);
        } else {
            model.addAttribute("readyReservations", Collections.emptyList());
        }
    }
}