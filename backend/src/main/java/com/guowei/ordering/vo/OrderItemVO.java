package com.guowei.ordering.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemVO {

    private Long id;

    private Long orderId;

    private Long dishId;

    private String dishName;

    private String dishImageUrl;

    private BigDecimal dishPrice;

    private Integer quantity;

    private BigDecimal amount;
}
