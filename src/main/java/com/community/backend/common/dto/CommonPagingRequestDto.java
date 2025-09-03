package com.community.backend.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonPagingRequestDto implements ICommonPagingDto {

    private int page;
    private int size;
    private String keyword;
    private String search;
    private long totalCount;
    private int totalPage;

    public CommonPagingRequestDto() {
        this.page = 0;
        this.size = 10;
        this.keyword = "";
        this.search = "";
        totalCount = 0;
        totalPage = 0;
    }

    @Override
    public void clean() {
        if (keyword == null || keyword == "undefined")
            this.keyword = "";
        if (search == null || search == "undefined")
            this.search = "";
    }
}
