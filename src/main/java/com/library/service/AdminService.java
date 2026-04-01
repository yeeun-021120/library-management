package com.library.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.domain.Book;
import com.library.domain.Rental;
import com.library.domain.User;
import com.library.mapper.AdminMapper;
import com.library.mapper.BookMapper;
import com.library.mapper.UserMapper;
import com.library.mapper.RentalMapper;

import lombok.RequiredArgsConstructor;

/**
 * 관리자 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final AdminMapper adminMapper;
    private final BookMapper bookMapper;
    private final UserMapper memberMapper;
    private final RentalMapper rentalMapper;

    /**
     * 전체 도서 수
     */
    public long getTotalBooksCount() {
        return adminMapper.countTotalBooks();
    }

    /**
     * 전체 회원 수
     */
    public long getTotalUsersCount() {
        return adminMapper.countTotalUsers();
    }

    /**
     * 현재 대여중인 도서 수
     */
    public long getActiveRentalsCount() {
        return adminMapper.countActiveRentals();
    }

    /**
     * 연체 도서 수
     */
    public long getOverdueRentalsCount() {
        return adminMapper.countOverdueRentals();
    }

    /**
     * 최근 대여 내역
     */
    public List<Rental> getRecentRentals(int limit) {
        return adminMapper.findRecentRentals(limit);
    }

    /**
     * 전체 회원 목록
     */
    public List<User> getAllUsers() {
        return adminMapper.findAllUsers();
    }

    /**
     * 회원 ID로 조회
     */
    public User getUserById(Long userId) {
        return memberMapper.findById(userId);
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public void updateUser(User user) {
        adminMapper.updateUser(user);
    }

    /**
     * 전체 대여 내역
     */
    public List<Rental> getAllRentals() {
        return adminMapper.findAllRentals();
    }

    /**
     * 도서 등록
     */
    @Transactional
    public void createBook(Book book) {
        bookMapper.insert(book);
    }

    /**
     * 도서 수정
     */
    @Transactional
    public void updateBook(Book book) {
        bookMapper.update(book);
    }

    /**
     * 도서 삭제
     */
    @Transactional
    public void deleteBook(Long bookId) {
        // 대여 이력이 있으면 삭제 불가 (FK 제약)
        bookMapper.delete(bookId);
    }
}