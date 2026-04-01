package com.library.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.library.domain.Book;
import com.library.domain.Category;
import com.library.service.BookService;
import com.library.service.CategoryService;
import com.library.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 도서 컨트롤러 (사용자용)
 */
@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    
    private final BookService bookService;
    private final CategoryService categoryService;
    private final UserService userService;
    
    /**
     * 도서 목록
     */
    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String availability,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        // 현재 로그인한 사용자 정보
        if (userDetails != null) {
            model.addAttribute("currentUser", userService.getUserByEmail(userDetails.getUsername()));
        }
        
        // 도서 검색
        List<Book> books = bookService.searchBooks(keyword, categoryId, availability);
        
        // 카테고리 목록
        List<Category> categories = categoryService.getAllCategories();
        
        model.addAttribute("books", books);
        model.addAttribute("categories", categories);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("availability", availability);
        
        return "book/list";
    }
    
    /**
     * 도서 상세
     */
    @GetMapping("/{id}")
    public String detail(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        // 현재 로그인한 사용자 정보
        if (userDetails != null) {
            model.addAttribute("currentUser", userService.getUserByEmail(userDetails.getUsername()));
        }
        
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        
        return "book/detail";
    }
}