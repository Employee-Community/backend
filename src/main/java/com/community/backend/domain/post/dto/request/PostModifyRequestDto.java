package com.community.backend.domain.post.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostModifyRequestDto(
    @NotNull
    Long memberIdx,

    @NotNull
    @Size(min = 5, max = 50, message = "제목은 5자 이상 50자 이하로 입력해주세요.")
    String title,

    @NotNull
    @Size(min = 5, max = 2000, message = "내용은 5자 이상 2000자 이하로 입력해주세요.")
    String contents
) {

}
