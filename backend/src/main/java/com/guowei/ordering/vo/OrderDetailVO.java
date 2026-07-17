package com.guowei.ordering.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailVO {

    private Long id;

    private String orderNumber;

    private Long userId;

    private Integer status;

    private BigDecimal amount;

    private Integer totalQuantity;

    private String remark;

    private LocalDateTime expectedTime;

    private String cancelReason;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<OrderItemVO> items;
}
