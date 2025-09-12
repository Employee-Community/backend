package com.community.backend.domain.postcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.community.backend.domain.postcategory.entity.PostCategory;

@Repository
public interface PostCategoryJpaRepository extends JpaRepository<PostCategory, Long> {

}
