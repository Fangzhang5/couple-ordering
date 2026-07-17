package com.guowei.ordering.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderListItemVO {

    private Long id;

    private String orderNumber;

    private Integer status;

    private BigDecimal amount;

    private Integer totalQuantity;

    private String remark;

    private LocalDateTime expectedTime;

    private LocalDateTime createTime;
}
