package com.guowei.ordering.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DishVO {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private String description;
    private Integer status;
}
