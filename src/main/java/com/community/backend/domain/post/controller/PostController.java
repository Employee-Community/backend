package com.community.backend.domain.post.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.community.backend.common.dto.ApiResponse;
import com.community.backend.common.dto.CommonPagingResponseDto;
import com.community.backend.common.security.jwt.JwtPayload;
import com.community.backend.domain.post.dto.request.PostCreateRequestDto;
import com.community.backend.domain.post.dto.request.PostModifyRequestDto;
import com.community.backend.domain.post.dto.request.PostPagingRequestDto;
import com.community.backend.domain.post.dto.response.PostResponseDto;
import com.community.backend.domain.post.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/v1/posts")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{postIdx}")
    public ResponseEntity<ApiResponse<PostResponseDto>> getPost(
            @PathVariable Long postIdx) {
        return ResponseEntity.ok(ApiResponse.success("ok", postService.getPost(postIdx)));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<CommonPagingResponseDto<List<PostResponseDto>>>> getPosts(
            @ModelAttribute PostPagingRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponse.success("ok", postService.getPosts(requestDto)));
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> getPopularPosts() {
        return ResponseEntity.ok(ApiResponse.success("ok", postService.getPopularPosts()));
    }

    @PostMapping("create")
    public ResponseEntity<ApiResponse<Void>> createPost(
            @Valid @RequestBody PostCreateRequestDto requestDto) {
        postService.createPost(requestDto);

        return ResponseEntity.ok(ApiResponse.success("ok", null));
    }

    @PutMapping("/{postIdx}")
    public ResponseEntity<ApiResponse<Void>> modifyPost(
            @Valid @RequestBody PostModifyRequestDto requestDto,
            @PathVariable Long postIdx,
            @AuthenticationPrincipal JwtPayload jwtPayload) {
        postService.modifyPost(requestDto, postIdx, jwtPayload);

        return ResponseEntity.ok(ApiResponse.success("ok", null));
    }

    @DeleteMapping("/{postIdx}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long postIdx,
            @AuthenticationPrincipal JwtPayload jwtPayload) { // TODO : JWT에서 추출이 가능하면 그 방법으로
        postService.deletePost(postIdx, jwtPayload);

        return ResponseEntity.ok(ApiResponse.success("ok", null));
    }
}
