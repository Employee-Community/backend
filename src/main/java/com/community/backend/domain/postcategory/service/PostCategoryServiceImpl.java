package com.community.backend.domain.postcategory.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.community.backend.domain.postcategory.dto.PostCategoryResponseDto;
import com.community.backend.domain.postcategory.entity.PostCategory;
import com.community.backend.domain.postcategory.repository.PostCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCategoryServiceImpl implements PostCategoryService {

    private final PostCategoryRepository repository;

    @Override
    public List<PostCategoryResponseDto> getCategories() {
        List<PostCategory> postCategories = repository.getAll();

        if (postCategories == null) {
            postCategories = new ArrayList<>();
        }

        return postCategories.stream().map(p -> new PostCategoryResponseDto(p)).toList();
    }
}
