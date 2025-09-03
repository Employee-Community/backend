package com.community.backend.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonPagingResponseDto<T> implements ICommonPagingDataDto<T> {

    private int page;
    private int size;
    private String keyword;
    private String search;
    private long totalCount;
    private int totalPage;
    private T data;

    public CommonPagingResponseDto() {
        this.page = 0;
        this.size = 10;
        this.keyword = "";
        this.search = "";
        totalCount = 0;
        totalPage = 0;
    }

    public CommonPagingResponseDto(CommonPagingRequestDto requestDto, T data) {
        this.page = requestDto.getPage();
        this.size = requestDto.getSize();
        this.keyword = requestDto.getKeyword();
        this.search = requestDto.getSearch();
        this.totalCount = requestDto.getTotalCount();
        this.totalPage = requestDto.getTotalPage();
        this.data = data;
    }

    @Override
    public void clean() {
        if (keyword == null || keyword == "undefined")
            this.keyword = "";
        if (search == null || search == "undefined")
            this.search = "";
    }
}
