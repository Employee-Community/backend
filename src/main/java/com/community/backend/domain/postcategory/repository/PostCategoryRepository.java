package com.community.backend.domain.postcategory.repository;

import java.util.List;

import com.community.backend.domain.postcategory.entity.PostCategory;

public interface PostCategoryRepository {

    List<PostCategory> getAll();
}
