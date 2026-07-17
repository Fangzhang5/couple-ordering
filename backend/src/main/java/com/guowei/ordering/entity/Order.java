package com.guowei.ordering.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {

    private Long id;

    private String orderNumber;

    private Long userId;

    private Integer status;

    private BigDecimal amount;

    private String remark;

    private LocalDateTime expectedTime;

    private String cancelReason;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
