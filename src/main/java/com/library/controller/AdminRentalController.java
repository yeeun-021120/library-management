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

import com.library.domain.Rental;
import com.library.service.RentalService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 대여 관리 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/admin/rental")
@RequiredArgsConstructor
public class AdminRentalController {

    private final RentalService rentalService;

    /**
     * 대여 목록
     */
    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            Model model) {
        
        List<Rental> rentals;
        
        if (status != null && !status.isEmpty()) {
            rentals = rentalService.getRentalsByStatus(status);
        } else if (keyword != null && !keyword.isEmpty()) {
            rentals = rentalService.searchRentals(keyword);
        } else {
            rentals = rentalService.getAllRentals();
        }
        
        // 통계
        int activeCount = rentalService.getActiveRentalsCount();
        int overdueCount = rentalService.getOverdueRentalsCount();
        int returnedCount = rentalService.getReturnedRentalsCount();
        
        model.addAttribute("rentals", rentals);
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("overdueCount", overdueCount);
        model.addAttribute("returnedCount", returnedCount);
        
        return "admin/rental/list";
    }

    /**
     * 관리자 반납 처리
     */
    @PostMapping("/return/{id}")
    public String returnBook(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            rentalService.adminReturnBook(id);
            redirectAttributes.addFlashAttribute("message", "반납 처리가 완료되었습니다.");
        } catch (Exception e) {
            log.error("반납 처리 실패", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin/rental/list";
    }

    /**
     * 연체 목록
     */
    @GetMapping("/overdue")
    public String overdueList(Model model) {
        List<Rental> overdueRentals = rentalService.getOverdueRentals();
        model.addAttribute("rentals", overdueRentals);
        return "admin/rental/overdue";
    }
}