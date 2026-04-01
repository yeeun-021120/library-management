package com.library.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.domain.Rental;
import com.library.domain.Reservation;
import com.library.domain.User;
import com.library.mapper.RentalMapper;
import com.library.mapper.ReservationMapper;
import com.library.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 회원 관리 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final RentalMapper rentalMapper;
    private final ReservationMapper reservationMapper;

    /**
     * 회원 목록
     */
    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String role,
            Model model) {
        
        List<User> users = userService.getAllUsers(keyword, status, role);
        
        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("role", role);
        
        return "admin/user/list";
    }

    /**
     * 회원 상세
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/user/detail";
    }

    /**
     * 회원 정지
     */
    @PostMapping("/{id}/suspend")
    public String suspend(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate restrictUntil,
            RedirectAttributes redirectAttributes) {
        
        try {
            userService.suspendUser(id, restrictUntil);
            redirectAttributes.addFlashAttribute("message", "회원이 정지되었습니다.");
        } catch (Exception e) {
            log.error("회원 정지 실패", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin/user/" + id;
    }

    /**
     * 회원 정지 해제
     */
    @PostMapping("/{id}/activate")
    public String activate(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            userService.activateUser(id);
            redirectAttributes.addFlashAttribute("message", "회원 정지가 해제되었습니다.");
        } catch (Exception e) {
            log.error("정지 해제 실패", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin/user/" + id;
    }

    /**
     * 회원 탈퇴 처리
     */
    @PostMapping("/{id}/withdraw")
    public String withdraw(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            userService.withdrawUser(id);
            redirectAttributes.addFlashAttribute("message", "회원이 탈퇴 처리되었습니다.");
            return "redirect:/admin/user/list";
        } catch (Exception e) {
            log.error("회원 탈퇴 실패", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/user/" + id;
        }
    }
}