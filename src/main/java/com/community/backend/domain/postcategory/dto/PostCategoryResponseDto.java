package com.community.backend.domain.postcategory.dto;

import com.community.backend.domain.postcategory.entity.PostCategory;

import lombok.Getter;

@Getter
public class PostCategoryResponseDto {

    private Long idx;
    private String name;
    private String imageUrl;

    public PostCategoryResponseDto(PostCategory category) {
        this.idx = category.getIdx();
        this.name = category.getName();
        this.imageUrl = category.getImageUrl();
    }
}
