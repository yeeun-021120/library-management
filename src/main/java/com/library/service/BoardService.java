package com.library.service;

import com.library.domain.Board;
import com.library.domain.BoardComment;
import com.library.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 게시판 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    
    private final BoardMapper boardMapper;
    
    // ========== 게시글 ==========
    
    /**
     * 게시글 목록 조회
     */
    public List<Board> getBoardList(String category) {
        return boardMapper.findAllByCategory(category);
    }
    
    /**
     * 게시글 상세 조회
     */
    @Transactional
    public Board getBoard(Long boardId) {
        // 조회수 증가
        boardMapper.incrementViews(boardId);
        return boardMapper.findById(boardId);
    }
    
    /**
     * 게시글 작성
     */
    @Transactional
    public void createBoard(Board board) {
        boardMapper.insert(board);
    }
    
    /**
     * 게시글 수정
     */
    @Transactional
    public void updateBoard(Board board) {
        boardMapper.update(board);
    }
    
    /**
     * 게시글 삭제
     */
    @Transactional
    public void deleteBoard(Long boardId) {
        boardMapper.delete(boardId);
    }
    
    /**
     * 상단 고정 토글 (관리자 전용)
     */
    @Transactional
    public void togglePin(Long boardId) {
        boardMapper.togglePin(boardId);
    }
    
    /**
     * 게시글 검색
     */
    public List<Board> searchBoards(String category, String keyword) {
        return boardMapper.search(category, keyword);
    }
    
    // ========== 댓글 ==========
    
    /**
     * 댓글 목록 조회
     */
    public List<BoardComment> getComments(Long boardId) {
        return boardMapper.findCommentsByBoardId(boardId);
    }
    
    /**
     * 댓글 작성
     */
    @Transactional
    public void createComment(BoardComment comment) {
        boardMapper.insertComment(comment);
    }
    
    /**
     * 댓글 수정
     */
    @Transactional
    public void updateComment(BoardComment comment) {
        boardMapper.updateComment(comment);
    }
    
    /**
     * 댓글 삭제
     */
    @Transactional
    public void deleteComment(Long commentId) {
        boardMapper.deleteComment(commentId);
    }
}