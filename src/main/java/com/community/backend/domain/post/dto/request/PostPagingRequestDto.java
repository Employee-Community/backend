package com.community.backend.domain.post.dto.request;

import com.community.backend.common.dto.CommonPagingRequestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostPagingRequestDto extends CommonPagingRequestDto {

    private Long memberIdx;
    private Long categoryIdx;

    public PostPagingRequestDto() {
        super();
        this.memberIdx = 0L;
        this.categoryIdx = 0L;
    }

    @Override
    public void clean() {
        super.clean();
        if (memberIdx == null)
            this.memberIdx = 0L;
        if (categoryIdx == null)
            this.categoryIdx = 0L;
    }
}