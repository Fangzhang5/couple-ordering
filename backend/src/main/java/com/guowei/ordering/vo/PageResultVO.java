package com.guowei.ordering.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PageResultVO<T> {

    private Integer page;

    private Integer pageSize;

    private Long total;

    private List<T> records;
}
