package com.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 게시판 도메인
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    private Long boardId;           // 게시글 ID
    private Long userId;            // 작성자 ID
    private String category;        // 카테고리 (NOTICE, FREE)
    private String title;           // 제목
    private String content;         // 내용
    private Integer views;          // 조회수
    private Boolean isPinned;       // 상단 고정 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    
    // 조인용 필드
    private String userName;        // 작성자 이름
    private Integer commentCount;   // 댓글 수
}