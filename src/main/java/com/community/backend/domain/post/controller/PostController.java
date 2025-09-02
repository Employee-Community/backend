package com.community.backend.domain.post.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.community.backend.common.dto.ApiResponse;
import com.community.backend.common.dto.CommonPagingResponseDto;
import com.community.backend.domain.post.dto.request.PostPagingRequestDto;
import com.community.backend.domain.post.dto.response.PostResponseDto;
import com.community.backend.domain.post.service.PostService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/v1/posts")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{postIdx}")
    public ResponseEntity<ApiResponse<PostResponseDto>> getPost(
        @PathVariable Long postIdx
    ) {
        return ResponseEntity.ok(ApiResponse.success("ok", postService.getPost(postIdx)));
    }
    
    @GetMapping("")
    public ResponseEntity<ApiResponse<CommonPagingResponseDto<List<PostResponseDto>>>> getPosts(
        @ModelAttribute PostPagingRequestDto requestDto
    ) {
        return ResponseEntity.ok(ApiResponse.success("ok", postService.getPosts(requestDto)));
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> getPopularPosts() {
        return ResponseEntity.ok(ApiResponse.success("ok", postService.getPopularPosts()));
    }
}
