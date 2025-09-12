package com.community.backend.domain.postcategory.service;

import java.util.List;

import com.community.backend.domain.postcategory.dto.PostCategoryResponseDto;

public interface PostCategoryService {

    List<PostCategoryResponseDto> getCategories();

}
