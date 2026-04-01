package com.library.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.domain.Category;
import com.library.mapper.CategoryMapper;

import lombok.RequiredArgsConstructor;

/**
 * 카테고리 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryMapper categoryMapper;

    /**
     * 전체 카테고리 목록 조회 (활성화된 것만)
     */
    public List<Category> getAllCategories() {
        return categoryMapper.findAll();
    }

    /**
     * 카테고리 ID로 조회
     */
    public Category getCategoryById(Long categoryId) {
        return categoryMapper.findById(categoryId);
    }

    /**
     * 카테고리 등록
     */
    @Transactional
    public void createCategory(Category category) {
        categoryMapper.insert(category);
    }

    /**
     * 카테고리 수정
     */
    @Transactional
    public void updateCategory(Category category) {
        categoryMapper.update(category);
    }

    /**
     * 카테고리 삭제 (비활성화)
     */
    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryMapper.delete(categoryId);
    }
}