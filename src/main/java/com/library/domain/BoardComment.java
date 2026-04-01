package com.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 게시판 댓글 도메인
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardComment {
    private Long commentId;         // 댓글 ID
    private Long boardId;           // 게시글 ID
    private Long userId;            // 작성자 ID
    private String content;         // 내용
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    
    // 조인용 필드
    private String userName;        // 작성자 이름
}