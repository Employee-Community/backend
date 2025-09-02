package com.community.backend.domain.post.dto.response;

import com.community.backend.domain.post.entity.Post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {

	private Long idx;

	private String name;

	private Long memberIdx;

	private Long categoryIdx;

	private String title;

	private String contents;

	private Integer viewCount = 0;

	private Integer state = 1;

    public PostResponseDto(Post post) {
        this.idx = post.getIdx();
        this.name = post.getName();
        this.memberIdx = post.getMember().getIdx();
        this.categoryIdx = post.getCategory().getIdx();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.viewCount = post.getViewCount();
        this.state = post.getState();
    }
}
