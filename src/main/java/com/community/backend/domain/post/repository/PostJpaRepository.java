package com.community.backend.domain.post.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.community.backend.domain.post.entity.Post;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Long> {

    // 단일 조회
    Optional<Post> findByIdx(Long idx);

    // 전체 검색
    Page<Post> findAll(Pageable pageable);
}