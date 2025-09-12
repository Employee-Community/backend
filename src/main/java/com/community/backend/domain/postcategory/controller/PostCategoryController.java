package com.community.backend.domain.postcategory.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.community.backend.common.dto.ApiResponse;
import com.community.backend.domain.postcategory.dto.PostCategoryResponseDto;
import com.community.backend.domain.postcategory.service.PostCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/post/category")
@RequiredArgsConstructor
public class PostCategoryController {

    private final PostCategoryService categoryService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<PostCategoryResponseDto>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.success("ok", categoryService.getCategories()));
    }
}
