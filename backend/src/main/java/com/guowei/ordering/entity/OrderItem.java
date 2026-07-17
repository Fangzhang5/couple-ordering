package com.guowei.ordering.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderItem {

    private Long id;

    private Long orderId;

    private Long dishId;

    private String dishName;

    private String dishImageUrl;

    private BigDecimal dishPrice;

    private Integer quantity;

    private BigDecimal amount;

    private LocalDateTime createTime;
}
