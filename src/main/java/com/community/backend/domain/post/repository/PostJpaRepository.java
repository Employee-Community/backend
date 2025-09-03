package com.community.backend.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.community.backend.domain.post.entity.Post;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Long> {

    Page<Post> findByTitleContaining(String title, Pageable pageable);
    Page<Post> findByTitleContainingAndCategory_Idx(String title, Long categoryIdx, Pageable pageable);

    Page<Post> findByContentsContaining(String contents, Pageable pageable);
    Page<Post> findByContentsContainingAndCategory_Idx(String contents, Long categoryIdx, Pageable pageable);

    Page<Post> findByMember_NameContaining(String memberName, Pageable pageable);
    Page<Post> findByMember_NameContainingAndCategory_Idx(String memberName, Long categoryIdx, Pageable pageable);
}