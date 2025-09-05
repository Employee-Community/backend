package com.community.backend.domain.post.repository;

import static com.community.backend.domain.post.entity.QPost.post;

import com.querydsl.core.types.dsl.BooleanExpression;

public class PostBooleanExpression {

    public static BooleanExpression memberIdxEq(Long memberIdx) {
        return memberIdx != null && memberIdx > 0L ? post.member.idx.eq(memberIdx) : null;
    }

    public static BooleanExpression titleEq(String title) {
        return title != null && !title.isEmpty() ? post.title.eq(title) : null;
    }

    public static BooleanExpression categoryEq(Long categoryIdx) {
        return categoryIdx != null && categoryIdx > 0L ? post.category.idx.eq(categoryIdx) : null;
    }

    public static BooleanExpression contentsEq(String contents) {
        return contents != null && !contents.isEmpty() ? post.contents.eq(contents) : null;
    }

    public static BooleanExpression memberNameEq(String memberName) {
        return memberName != null && !memberName.isEmpty() ? post.member.name.eq(memberName) : null;
    }

    public static BooleanExpression containsKeyword(String search, String keyword) {
        if (keyword == null || keyword.isEmpty())
            return null;
        return switch (search) {
            case "title" -> post.title.contains(keyword);
            case "contents" -> post.contents.contains(keyword);
            case "titlecontents" -> post.title.contains(keyword).or(post.contents.contains(keyword));
            case "member" -> post.member.name.contains(keyword);
            default -> null;
        };
    }

    public static BooleanExpression stateEq(Integer state) {
        return state != null ? post.state.eq(state) : null;
    }
}
