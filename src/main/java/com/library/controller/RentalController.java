package com.library.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.domain.Rental;
import com.library.service.RentalService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 대여 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/rental")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    /**
     * 내 대여 목록 페이지
     */
    @GetMapping("/my")
    public String myRentals(Principal principal, Model model) {
        String email = principal.getName();
        List<Rental> rentals = rentalService.getUserRentals(email);
        
        model.addAttribute("rentals", rentals);
        return "rental/my";
    }

    /**
     * 도서 대여 처리
     */
    @PostMapping("/rent/{bookId}")
    public String rentBook(
            @PathVariable Long bookId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        try {
            // 예약자인지 확인
            String email = userDetails.getUsername();
            if (rentalService.hasActiveReservation(email, bookId)) {
                // 예약자 대여 (RESERVED 도서)
                rentalService.rentReservedBook(bookId, userDetails);
                redirectAttributes.addFlashAttribute("message", "예약하신 도서가 대여되었습니다!");
            } else {
                // 일반 대여 (AVAILABLE 도서)
                rentalService.rentBook(bookId, userDetails);
                redirectAttributes.addFlashAttribute("message", "도서가 대여되었습니다.");
            }
        } catch (Exception e) {
            log.error("대여 실패", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/rental/my";
    }

    /**
     * 도서 반납 처리
     */
    @PostMapping("/return/{rentalId}")
    public String returnBook(
            @PathVariable Long rentalId,
            RedirectAttributes redirectAttributes) {
        
        try {
            rentalService.returnBook(rentalId);
            redirectAttributes.addFlashAttribute("message", "반납이 완료되었습니다.");
        } catch (Exception e) {
            log.error("반납 실패", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/rental/my";
    }
}