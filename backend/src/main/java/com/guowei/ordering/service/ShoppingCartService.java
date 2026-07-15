package com.guowei.ordering.service;

import com.guowei.ordering.dto.AddCartItemDTO;
import com.guowei.ordering.dto.UpdateCartItemQuantityDTO;
import com.guowei.ordering.vo.CartSummaryVO;

public interface ShoppingCartService {

    /**
     * 查询当前用户购物车
     */
    CartSummaryVO getCart(Long userId);

    /**
     * 加入购物车
     */
    CartSummaryVO addCartItem(Long userId, AddCartItemDTO addCartItemDTO);

    /**
     * 修改购物车项数量
     */
    CartSummaryVO updateCartItemQuantity(Long userId, Long cartItemId, UpdateCartItemQuantityDTO updateDTO);

    /**
     * 删除单个购物车项
     */
    CartSummaryVO deleteCartItem(Long userId, Long cartItemId);

    /**
     * 清空当前用户购物车
     */
    void clearCart(Long userId);
}
