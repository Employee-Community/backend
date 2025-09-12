package com.community.backend.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.community.backend.domain.post.entity.PostComment;

@Repository
public interface PostCommentJpaRepository extends JpaRepository<PostComment, Long> {

	Page<PostComment> findByMemberIdx(Long memberIdx, Pageable pageable);

	Page<PostComment> findByPost_Idx(Long postIdx, Pageable pageable);
}
