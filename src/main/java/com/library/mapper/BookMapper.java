package com.library.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.library.domain.Book;

/**
 * 도서 매퍼
 */
@Mapper
public interface BookMapper {
    
    /**
     * 전체 도서 목록 조회
     */
    List<Book> findAll();
    
    /**
     * 도서 검색 (복합 조건)
     */
    List<Book> searchBooks(@Param("keyword") String keyword, 
                          @Param("categoryId") Long categoryId,
                          @Param("availability") String availability);
    
    /**
     * 도서 상세 조회
     */
    Book findById(@Param("bookId") Long bookId);
    
    /**
     * 카테고리별 도서 목록
     */
    List<Book> findByCategory(@Param("categoryId") Long categoryId);
    
    /**
     * 도서 등록
     */
    void insert(Book book);
    
    /**
     * 도서 수정
     */
    void update(Book book);
    
    /**
     * 도서 삭제
     */
    void delete(@Param("bookId") Long bookId);
    
    /**
     * 전체 도서 수
     */
    int countAll();
    
    /**
     * 상태별 도서 수
     */
    int countByStatus(@Param("status") String status);
    
    /**
     * 인기 도서 TOP N
     */
    List<Book> findPopularBooks(@Param("limit") int limit);
    
    /**
     * 최신 도서
     */
    List<Book> findRecentBooks(@Param("limit") int limit);
}