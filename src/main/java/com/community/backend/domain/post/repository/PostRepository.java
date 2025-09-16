package com.community.backend.domain.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.community.backend.domain.post.dto.request.PostPagingRequestDto;
import com.community.backend.domain.post.entity.Post;

public interface PostRepository {

    // 단일 조회
    Optional<Post> getPostByIdx(Long idx);

    List<Post> getPostsByIdxs(List<Long> idxs);

    // 전체 검색
    Page<Post> getPosts(PostPagingRequestDto requestDto, Pageable pageable);

    // 게시글 생성
    void createPost(Post post, Long memberIdx, Long categoryIdx);

    // 게시글 저장(수정, 삭제)
    void savePost(Post post);

    void deleteAllPosts(Long memberIdx);
}
