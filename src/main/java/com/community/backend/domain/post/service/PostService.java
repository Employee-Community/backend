package com.community.backend.domain.post.service;

import java.util.List;

import com.community.backend.common.dto.CommonPagingResponseDto;
import com.community.backend.common.security.jwt.JwtPayload;
import com.community.backend.domain.post.dto.request.PostCreateRequestDto;
import com.community.backend.domain.post.dto.request.PostModifyRequestDto;
import com.community.backend.domain.post.dto.request.PostPagingRequestDto;
import com.community.backend.domain.post.dto.response.PostResponseDto;

public interface PostService {

    PostResponseDto getPost(Long postIdx);

    CommonPagingResponseDto<List<PostResponseDto>> getPosts(PostPagingRequestDto requestDto);

    List<PostResponseDto> getPopularPosts();

    void createPost(PostCreateRequestDto requestDto);

    void modifyPost(PostModifyRequestDto requestDto, Long postIdx, JwtPayload jwtPayload);

    void deletePost(Long postIdx, JwtPayload jwtPayload);
}
