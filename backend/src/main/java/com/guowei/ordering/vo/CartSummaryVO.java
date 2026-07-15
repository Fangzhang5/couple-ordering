package com.guowei.ordering.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class CartSummaryVO {

    /**
     * 购物车明细
     */
    private List<CartItemVO> items;

    /**
     * 所有菜品数量之和
     */
    private Integer totalQuantity;

    /**
     * 购物车总金额
     */
    private BigDecimal totalAmount;
}
