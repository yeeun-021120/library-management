package com.library.controller;

import com.library.domain.Board;
import com.library.domain.BoardComment;
import com.library.domain.User;
import com.library.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 게시판 컨트롤러 (사용자용)
 */
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    
    private final BoardService boardService;
    private final com.library.service.UserService userService;
    
    /**
     * 공지사항 목록
     */
    @GetMapping("/notice")
    public String noticeList(
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        List<Board> boards;
        if (keyword != null && !keyword.trim().isEmpty()) {
            boards = boardService.searchBoards("NOTICE", keyword);
        } else {
            boards = boardService.getBoardList("NOTICE");
        }
        
        model.addAttribute("boards", boards);
        model.addAttribute("category", "NOTICE");
        model.addAttribute("keyword", keyword);
        return "board/list";
    }
    
    /**
     * 자유게시판 목록
     */
    @GetMapping("/free")
    public String freeList(
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        List<Board> boards;
        if (keyword != null && !keyword.trim().isEmpty()) {
            boards = boardService.searchBoards("FREE", keyword);
        } else {
            boards = boardService.getBoardList("FREE");
        }
        
        model.addAttribute("boards", boards);
        model.addAttribute("category", "FREE");
        model.addAttribute("keyword", keyword);
        return "board/list";
    }
    
    /**
     * 게시글 상세
     */
    @GetMapping("/{boardId}")
    public String detail(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        Board board = boardService.getBoard(boardId);
        List<BoardComment> comments = boardService.getComments(boardId);
        
        User currentUser = null;
        if (userDetails != null) {
            currentUser = userService.findByEmail(userDetails.getUsername());
        }
        
        model.addAttribute("board", board);
        model.addAttribute("comments", comments);
        model.addAttribute("currentUser", currentUser);
        return "board/detail";
    }
    
    /**
     * 게시글 작성 폼 (자유게시판만)
     */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("board", new Board());
        return "board/form";
    }
    
    /**
     * 게시글 작성 처리
     */
    @PostMapping("/new")
    public String create(
            @ModelAttribute Board board,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.findByEmail(userDetails.getUsername());
        board.setUserId(user.getUserId());
        board.setCategory("FREE");  // 사용자는 자유게시판만 작성 가능
        board.setIsPinned(false);
        
        boardService.createBoard(board);
        return "redirect:/board/free";
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
        Board board = boardService.getBoard(boardId);
        User user = userService.findByEmail(userDetails.getUsername());
        
        // 작성자 본인만 수정 가능
        if (!board.getUserId().equals(user.getUserId())) {
            return "redirect:/board/" + boardId;
        }
        
        model.addAttribute("board", board);
        return "board/form";
    }
    
    /**
     * 게시글 수정 처리
     */
    @PostMapping("/{boardId}/edit")
    public String update(
            @PathVariable Long boardId,
            @ModelAttribute Board board,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Board existing = boardService.getBoard(boardId);
        User user = userService.findByEmail(userDetails.getUsername());
        
        // 작성자 본인만 수정 가능
        if (!existing.getUserId().equals(user.getUserId())) {
            return "redirect:/board/" + boardId;
        }
        
        board.setBoardId(boardId);
        boardService.updateBoard(board);
        return "redirect:/board/" + boardId;
    }
    
    /**
     * 게시글 삭제
     */
    @PostMapping("/{boardId}/delete")
    public String delete(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Board board = boardService.getBoard(boardId);
        User user = userService.findByEmail(userDetails.getUsername());
        String category = board.getCategory();
        
        // 작성자 본인만 삭제 가능
        if (!board.getUserId().equals(user.getUserId())) {
            return "redirect:/board/" + boardId;
        }
        
        boardService.deleteBoard(boardId);
        return "redirect:/board/" + (category.equals("NOTICE") ? "notice" : "free");
    }
    
    /**
     * 댓글 작성
     */
    @PostMapping("/{boardId}/comment")
    public String createComment(
            @PathVariable Long boardId,
            @RequestParam String content,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.findByEmail(userDetails.getUsername());
        
        BoardComment comment = BoardComment.builder()
                .boardId(boardId)
                .userId(user.getUserId())
                .content(content)
                .build();
        
        boardService.createComment(comment);
        return "redirect:/board/" + boardId;
    }
    
    /**
     * 댓글 삭제
     */
    @PostMapping("/{boardId}/comment/{commentId}/delete")
    public String deleteComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId
    ) {
        boardService.deleteComment(commentId);
        return "redirect:/board/" + boardId;
    }
}