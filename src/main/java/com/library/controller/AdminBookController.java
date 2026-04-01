package com.library.controller;

import com.library.domain.Book;
import com.library.domain.Category;
import com.library.service.BookService;
import com.library.service.CategoryService;
import com.library.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 도서 관리 컨트롤러 (관리자용)
 */
@Controller
@RequestMapping("/admin/book")
@RequiredArgsConstructor
public class AdminBookController {
    
    private final BookService bookService;
    private final CategoryService categoryService;
    
    /**
     * 도서 목록
     */
    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String availability,
            Model model
    ) {
        List<Book> books = bookService.searchBooks(keyword, categoryId, availability);
        List<Category> categories = categoryService.getAllCategories();
        
        model.addAttribute("books", books);
        model.addAttribute("categories", categories);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("availability", availability);
        
        return "admin/book/list";
    }
    
    /**
     * 도서 등록 폼
     */
    @GetMapping("/new")
    public String newForm(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("book", new Book());
        return "admin/book/form";
    }
    
    /**
     * 도서 등록 처리
     */
    @PostMapping("/new")
    public String create(
            @ModelAttribute Book book,
            @RequestParam("coverImageFile") MultipartFile coverImageFile,
            @RequestParam Integer quantity
    ) throws IOException {
        // 표지 이미지 업로드
        if (!coverImageFile.isEmpty()) {
            String imagePath = FileUploadUtil.uploadFile(coverImageFile);
            book.setCoverImage(imagePath);
        }
        
        bookService.createBook(book, quantity);
        return "redirect:/admin/book/list";
    }
    
    /**
     * 도서 수정 폼
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        List<Category> categories = categoryService.getAllCategories();
        
        model.addAttribute("book", book);
        model.addAttribute("categories", categories);
        return "admin/book/form";
    }
    
    /**
     * 도서 수정 처리
     */
    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @ModelAttribute Book book,
            @RequestParam(value = "coverImageFile", required = false) MultipartFile coverImageFile
    ) throws IOException {
        book.setBookId(id);
        
        // 새 이미지가 업로드된 경우
        if (coverImageFile != null && !coverImageFile.isEmpty()) {
            // 기존 이미지 삭제
            Book existingBook = bookService.getBookById(id);
            if (existingBook.getCoverImage() != null) {
                FileUploadUtil.deleteFile(existingBook.getCoverImage());
            }
            
            // 새 이미지 업로드
            String imagePath = FileUploadUtil.uploadFile(coverImageFile);
            book.setCoverImage(imagePath);
        } else {
            // 이미지를 변경하지 않은 경우 기존 이미지 유지
            Book existingBook = bookService.getBookById(id);
            book.setCoverImage(existingBook.getCoverImage());
        }
        
        bookService.updateBook(book);
        return "redirect:/admin/book/list";
    }
    
    /**
     * 도서 삭제
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        // 이미지 파일도 함께 삭제
        Book book = bookService.getBookById(id);
        if (book.getCoverImage() != null) {
            FileUploadUtil.deleteFile(book.getCoverImage());
        }
        
        bookService.deleteBook(id);
        return "redirect:/admin/book/list";
    }
    
    /**
     * 복본 추가 폼
     */
    @GetMapping("/{id}/add-copies")
    public String addCopiesForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "admin/book/add-copies";
    }
    
    /**
     * 복본 추가 처리
     */
    @PostMapping("/{id}/add-copies")
    public String addCopies(
            @PathVariable Long id,
            @RequestParam Integer quantity
    ) {
        bookService.addCopies(id, quantity);
        return "redirect:/admin/book/list";
    }
}