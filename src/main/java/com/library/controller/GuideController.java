package com.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 이용 안내 컨트롤러
 */
@Controller
@RequestMapping("/guide")
public class GuideController {

    /**
     * 이용 안내 페이지
     */
    @GetMapping("/how-to-use")
    public String howToUse() {
        return "guide/how-to-use";
    }
}