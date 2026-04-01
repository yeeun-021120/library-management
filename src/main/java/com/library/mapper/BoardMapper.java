package com.library.mapper;

import com.library.domain.Board;
import com.library.domain.BoardComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 게시판 Mapper
 */
@Mapper
public interface BoardMapper {
    
    // ========== 게시글 ==========
    
    /**
     * 게시글 목록 조회 (카테고리별)
     */
    List<Board> findAllByCategory(@Param("category") String category);
    
    /**
     * 게시글 상세 조회
     */
    Board findById(@Param("boardId") Long boardId);
    
    /**
     * 게시글 작성
     */
    int insert(Board board);
    
    /**
     * 게시글 수정
     */
    int update(Board board);
    
    /**
     * 게시글 삭제 (Soft Delete)
     */
    int delete(@Param("boardId") Long boardId);
    
    /**
     * 조회수 증가
     */
    int incrementViews(@Param("boardId") Long boardId);
    
    /**
     * 상단 고정 토글
     */
    int togglePin(@Param("boardId") Long boardId);
    
    /**
     * 검색 (제목 + 내용)
     */
    List<Board> search(@Param("category") String category, @Param("keyword") String keyword);
    
    // ========== 댓글 ==========
    
    /**
     * 댓글 목록 조회 (게시글별)
     */
    List<BoardComment> findCommentsByBoardId(@Param("boardId") Long boardId);
    
    /**
     * 댓글 작성
     */
    int insertComment(BoardComment comment);
    
    /**
     * 댓글 수정
     */
    int updateComment(BoardComment comment);
    
    /**
     * 댓글 삭제 (Soft Delete)
     */
    int deleteComment(@Param("commentId") Long commentId);
}