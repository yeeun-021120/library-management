package com.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.domain.User;
import com.library.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 회원 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final UserService userService;

    /**
     * 회원가입 폼
     */
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "member/signup";
    }

    /**
     * 회원가입 처리
     */
    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/member/login";
        } catch (Exception e) {
            log.error("회원가입 실패", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/member/signup";
        }
    }

    /**
     * 로그인 폼
     */
    @GetMapping("/login")
    public String loginForm() {
        return "member/login";
    }
}