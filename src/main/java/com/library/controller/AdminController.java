package com.library.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.domain.Book;
import com.library.domain.Category;
import com.library.domain.Rental;
import com.library.service.BookService;
import com.library.service.CategoryService;
import com.library.service.RentalService;
import com.library.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 관리자 메인 컨트롤러
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BookService bookService;
    private final UserService userService;
    private final RentalService rentalService;
    private final CategoryService categoryService;

    /**
     * 관리자 대시보드
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // 통계 데이터
        int totalBooks = bookService.getTotalBooks();
        int totalUsers = userService.getTotalUsers();
        int rentedCount = rentalService.getActiveRentalsCount();
        int overdueCount = rentalService.getOverdueRentalsCount();

        // 최근 대여 목록 (최대 10개)
        List<Rental> recentRentals = rentalService.getAllRentals();
        if (recentRentals.size() > 10) {
            recentRentals = recentRentals.subList(0, 10);
        }

        // 연체 목록
        List<Rental> overdueRentalList = rentalService.getOverdueRentals();

        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("rentedCount", rentedCount);
        model.addAttribute("overdueCount", overdueCount);
        model.addAttribute("recentRentals", recentRentals);
        model.addAttribute("overdueRentals", overdueRentalList);

        return "admin/dashboard";
    }

    /**
     * 도서 관리 페이지
     */
    @GetMapping("/books")
    public String bookManagement(Model model) {
        List<Book> books = bookService.getAllBooks();
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("books", books);
        model.addAttribute("categories", categories);

        return "admin/books";
    }
    
    
}