package com.guowei.ordering.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShoppingCart {

    private Long id;

    private Long userId;

    private Long dishId;

    private Integer quantity;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
