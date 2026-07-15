package com.guowei.ordering.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemVO {
    /**
     * 购物车项ID
     */
    private Long id;

    /**
     * 菜品ID
     */
    private Long dishId;

    /**
     * 菜品名称
     */
    private String dishName;

    /**
     * 菜品图片
     */
    private String dishImageUrl;

    /**
     * 菜品当前单价
     */
    private BigDecimal dishPrice;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 当前购物车项金额
     */
    private BigDecimal amount;
}
