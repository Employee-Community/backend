package com.community.backend.domain.post.dto.response;

import java.time.LocalDateTime;

import com.community.backend.domain.member.entity.Member;
import com.community.backend.domain.post.entity.Post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {

    private Long idx;

    private Long memberIdx;

    private String nickname;

    private Long categoryIdx;

    private String categoryName;

    private String title;

    private String contents;

    private Integer viewCount = 0;

    private Integer state = 1;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public PostResponseDto(Post post, Long memberIdx, String nickname) {
        this.idx = post.getIdx();
        this.memberIdx = memberIdx;
        this.nickname = nickname;
        this.categoryIdx = post.getCategory().getIdx();
        this.categoryName = post.getCategory().getName();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.viewCount = post.getViewCount();
        this.state = post.getState();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

    public PostResponseDto(Post post, Member member) {
        this.idx = post.getIdx();
        this.memberIdx = member != null ? member.getIdx() : 0L;
        this.nickname = member != null ? member.getNickname() : "NAN";
        this.categoryIdx = post.getCategory().getIdx();
        this.categoryName = post.getCategory().getName();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.viewCount = post.getViewCount();
        this.state = post.getState();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
