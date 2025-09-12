package com.community.backend.domain.postcategory.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.community.backend.domain.postcategory.entity.PostCategory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostCategoryRepositoryImpl implements PostCategoryRepository {

    private final PostCategoryJpaRepository jpaRepository;

    @Override
    public List<PostCategory> getAll() {
        return jpaRepository.findAll();
    }
}
