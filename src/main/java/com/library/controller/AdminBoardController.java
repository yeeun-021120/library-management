package com.library.controller;

import com.library.domain.Board;
import com.library.domain.User;
import com.library.service.BoardService;
import com.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 게시판 관리 컨트롤러 (관리자용)
 */
@Controller
@RequestMapping("/admin/board")
@RequiredArgsConstructor
public class AdminBoardController {
    
    private final BoardService boardService;
    private final UserService userService;
    
    /**
     * 게시판 목록 (전체)
     */
    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        // 현재 로그인한 사용자 정보
        User currentUser = userService.findByEmail(userDetails.getUsername());
        
        List<Board> boards;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            boards = boardService.searchBoards(category != null ? category : "FREE", keyword);
        } else if (category != null && !category.trim().isEmpty()) {
            boards = boardService.getBoardList(category);
        } else {
            // 전체 조회 (공지사항 + 자유게시판)
            List<Board> notices = boardService.getBoardList("NOTICE");
            List<Board> frees = boardService.getBoardList("FREE");
            notices.addAll(frees);
            boards = notices;
        }
        
        model.addAttribute("boards", boards);
        model.addAttribute("category", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentUser", currentUser);
        return "admin/board/list";
    }
    
    /**
     * 공지사항 작성 폼
     */
    @GetMapping("/notice/new")
    public String noticeForm(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        User currentUser = userService.findByEmail(userDetails.getUsername());
        
        model.addAttribute("board", new Board());
        model.addAttribute("category", "NOTICE");
        model.addAttribute("currentUser", currentUser);
        return "admin/board/form";
    }
    
    /**
     * 공지사항 작성 처리
     */
    @PostMapping("/notice/new")
    public String createNotice(
            @ModelAttribute Board board,
            @RequestParam(required = false, defaultValue = "false") Boolean isPinned,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.findByEmail(userDetails.getUsername());
        board.setUserId(user.getUserId());
        board.setCategory("NOTICE");
        board.setIsPinned(isPinned);
        
        boardService.createBoard(board);
        return "redirect:/admin/board/list?category=NOTICE";
    }
    
    /**
     * 게시글 수정 폼
     */
    @GetMapping("/{boardId}/edit")
    public String editForm(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        User currentUser = userService.findByEmail(userDetails.getUsername());
        Board board = boardService.getBoard(boardId);
        
        model.addAttribute("board", board);
        model.addAttribute("currentUser", currentUser);
        return "admin/board/form";
    }
    
    /**
     * 게시글 수정 처리
     */
    @PostMapping("/{boardId}/edit")
    public String update(
            @PathVariable Long boardId,
            @ModelAttribute Board board,
            @RequestParam(required = false, defaultValue = "false") Boolean isPinned
    ) {
        Board existing = boardService.getBoard(boardId);
        
        board.setBoardId(boardId);
        board.setCategory(existing.getCategory());
        board.setIsPinned(isPinned);
        
        boardService.updateBoard(board);
        return "redirect:/admin/board/list?category=" + existing.getCategory();
    }
    
    /**
     * 상단 고정 토글
     */
    @PostMapping("/{boardId}/toggle-pin")
    public String togglePin(@PathVariable Long boardId) {
        boardService.togglePin(boardId);
        Board board = boardService.getBoard(boardId);
        return "redirect:/admin/board/list?category=" + board.getCategory();
    }
    
    /**
     * 게시글 강제 삭제 (관리자)
     */
    @PostMapping("/{boardId}/delete")
    public String delete(@PathVariable Long boardId) {
        Board board = boardService.getBoard(boardId);
        String category = board.getCategory();
        boardService.deleteBoard(boardId);
        return "redirect:/admin/board/list?category=" + category;
    }
}