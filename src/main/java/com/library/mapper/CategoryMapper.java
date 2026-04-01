package com.library.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.library.domain.Category;

/**
 * 카테고리 Mapper 인터페이스
 */
@Mapper
public interface CategoryMapper {
    
    /**
     * 전체 카테고리 목록 조회 (활성화된 것만)
     */
    List<Category> findAll();
    
    /**
     * 카테고리 ID로 조회
     */
    Category findById(@Param("categoryId") Long categoryId);
    
    /**
     * 카테고리 등록
     */
    int insert(Category category);
    
    /**
     * 카테고리 수정
     */
    int update(Category category);
    
    /**
     * 카테고리 삭제 (비활성화)
     */
    int delete(@Param("categoryId") Long categoryId);
}