package com.library.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.domain.Book;
import com.library.mapper.BookCopyMapper;
import com.library.mapper.BookMapper;

import lombok.RequiredArgsConstructor;

/**
 * 도서 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookMapper bookMapper;
    private final BookCopyMapper bookCopyMapper;

    /**
     * 전체 도서 목록 조회
     */
    public List<Book> getAllBooks() {
        return bookMapper.findAll();
    }

    /**
     * 도서 검색 (제목, 저자, 카테고리, 대여 가능 여부)
     */
    public List<Book> searchBooks(String keyword, Long categoryId, String availability) {
        return bookMapper.searchBooks(keyword, categoryId, availability);
    }

    /**
     * 도서 상세 조회
     */
    public Book getBookById(Long bookId) {
        Book book = bookMapper.findById(bookId);
        if (book == null) {
            throw new IllegalStateException("도서를 찾을 수 없습니다.");
        }
        return book;
    }

    /**
     * 카테고리별 도서 목록
     */
    public List<Book> getBooksByCategory(Long categoryId) {
        return bookMapper.findByCategory(categoryId);
    }

    // ========== 관리자용 메서드 ==========

    /**
     * 도서 등록
     */
    @Transactional
    public void createBook(Book book, int copyCount) {
        // 1. 도서 정보 등록
        bookMapper.insert(book);

        // 2. 복본 등록
        for (int i = 0; i < copyCount; i++) {
            bookCopyMapper.insertCopy(book.getBookId());
        }
    }

    /**
     * 도서 수정
     */
    @Transactional
    public void updateBook(Book book) {
        Book existing = bookMapper.findById(book.getBookId());
        if (existing == null) {
            throw new IllegalStateException("도서를 찾을 수 없습니다.");
        }
        bookMapper.update(book);
    }

    /**
     * 도서 삭제
     */
    @Transactional
    public void deleteBook(Long bookId) {
        Book book = bookMapper.findById(bookId);
        if (book == null) {
            throw new IllegalStateException("도서를 찾을 수 없습니다.");
        }

        // 대여중인 복본이 있는지 확인
        int rentedCount = bookCopyMapper.countRentedCopies(bookId);
        if (rentedCount > 0) {
            throw new IllegalStateException("대여중인 복본이 있어 삭제할 수 없습니다.");
        }

        // 복본 삭제
        bookCopyMapper.deleteByBookId(bookId);

        // 도서 삭제
        bookMapper.delete(bookId);
    }

    /**
     * 복본 추가 (1개)
     */
    @Transactional
    public void addCopy(Long bookId) {
        Book book = bookMapper.findById(bookId);
        if (book == null) {
            throw new IllegalStateException("도서를 찾을 수 없습니다.");
        }
        bookCopyMapper.insertCopy(bookId);
    }

    /**
     * 복본 추가 (여러 개)
     */
    @Transactional
    public void addCopies(Long bookId, Integer quantity) {
        Book book = bookMapper.findById(bookId);
        if (book == null) {
            throw new IllegalStateException("도서를 찾을 수 없습니다.");
        }
        
        if (quantity != null && quantity > 0) {
            for (int i = 0; i < quantity; i++) {
                bookCopyMapper.insertCopy(bookId);
            }
        }
    }

    /**
     * 도서 통계
     */
    public int getTotalBooks() {
        return bookMapper.countAll();
    }

    public int getBooksByStatus(String status) {
        return bookMapper.countByStatus(status);
    }

    /**
     * 인기 도서 TOP N
     */
    public List<Book> getPopularBooks(int limit) {
        return bookMapper.findPopularBooks(limit);
    }

    /**
     * 최신 도서
     */
    public List<Book> getRecentBooks(int limit) {
        return bookMapper.findRecentBooks(limit);
    }
}