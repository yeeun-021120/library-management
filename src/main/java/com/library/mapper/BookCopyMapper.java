package com.library.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 도서 복본 Mapper
 */
@Mapper
public interface BookCopyMapper {
    
    /**
     * 복본 등록
     */
    int insertCopy(@Param("bookId") Long bookId);
    
    /**
     * 복본 상태 변경
     */
    int updateStatus(
        @Param("copyId") Long copyId,
        @Param("status") String status
    );
    
    /**
     * 도서 ID로 복본 삭제
     */
    int deleteByBookId(@Param("bookId") Long bookId);
    
    /**
     * 대여중인 복본 수 조회
     */
    int countRentedCopies(@Param("bookId") Long bookId);
    
    /**
     * 특정 상태의 복본 수 조회
     */
    int countByStatus(
        @Param("bookId") Long bookId,
        @Param("status") String status
    );
}