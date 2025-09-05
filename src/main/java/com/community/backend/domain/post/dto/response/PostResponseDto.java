package com.community.backend.domain.post.dto.response;

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

    public PostResponseDto(Post post) {
        this.idx = post.getIdx();
        this.memberIdx = post.getMember().getIdx();
        this.nickname = post.getMember().getNickname();
        this.categoryIdx = post.getCategory().getIdx();
        this.categoryName = post.getCategory().getName();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.viewCount = post.getViewCount();
        this.state = post.getState();
    }
}
