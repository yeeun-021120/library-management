package com.library.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

/**
 * 메인 홈 컨트롤러
 */
@Slf4j
@Controller
public class HomeController {

    /**
     * 메인 페이지 - 권한에 따라 리다이렉트
     */
    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // 로그인된 경우 권한에 따라 분기
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            
            if (isAdmin) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/book/list";
            }
        }
        
        // 비로그인 상태면 로그인 페이지로
        return "redirect:/member/login";
    }

    /**
     * 에러 페이지
     */
    @GetMapping("/error")
    public String error() {
        return "error";
    }
}