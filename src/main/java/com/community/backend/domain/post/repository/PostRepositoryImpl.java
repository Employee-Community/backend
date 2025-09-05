package com.community.backend.domain.post.repository;

import static com.community.backend.domain.category.entity.QCategory.category;
import static com.community.backend.domain.member.entity.QMember.member;
import static com.community.backend.domain.post.entity.QPost.post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.community.backend.domain.post.dto.request.PostPagingRequestDto;
import com.community.backend.domain.post.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Post> getPostByIdx(Long idx) {
        return jpaRepository.findByIdx(idx);
    };

    @Override
    public Page<Post> getPosts(PostPagingRequestDto requestDto, Pageable pageable) {
        List<Post> result = queryFactory
                .selectFrom(post)
                .leftJoin(post.member, member)
                .leftJoin(post.category, category)
                .where(
                        PostBooleanExpression.memberIdxEq(requestDto.getMemberIdx()),
                        PostBooleanExpression.categoryEq(requestDto.getCategoryIdx()),
                        PostBooleanExpression.containsKeyword(requestDto.getSearch(), requestDto.getKeyword()),
                        PostBooleanExpression.stateEq(1))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdAt.desc())
                .fetch();

        long totalCount = queryFactory
                .select(post.count())
                .from(post)
                .leftJoin(post.member, member)
                .leftJoin(post.category, category)
                .where(
                        PostBooleanExpression.memberIdxEq(requestDto.getMemberIdx()),
                        PostBooleanExpression.categoryEq(requestDto.getCategoryIdx()),
                        PostBooleanExpression.containsKeyword(requestDto.getSearch(), requestDto.getKeyword()),
                        PostBooleanExpression.stateEq(1))
                .fetchOne();

        return new PageImpl<>(result, pageable, totalCount);
    };

    @Override
    public void createPost(Post postParam, Long memberIdx, Long categoryIdx) {
        LocalDateTime now = LocalDateTime.now();

        queryFactory.insert(post)
                .columns(post.title, post.contents, post.member.idx, post.category.idx, post.createdAt, post.updatedAt,
                        post.viewCount, post.state)
                .values(postParam.getTitle(), postParam.getContents(), memberIdx, categoryIdx, now, now,
                        postParam.getViewCount(), postParam.getState())
                .execute();
    }

    @Override
    public void savePost(Post post) {
        jpaRepository.save(post);
    }
}