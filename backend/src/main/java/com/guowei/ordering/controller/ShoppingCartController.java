package com.guowei.ordering.controller;

import com.guowei.ordering.common.result.Result;
import com.guowei.ordering.dto.AddCartItemDTO;
import com.guowei.ordering.dto.UpdateCartItemQuantityDTO;
import com.guowei.ordering.service.ShoppingCartService;
import com.guowei.ordering.vo.CartSummaryVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart/items")
@Validated
public class ShoppingCartController {

    private static final String DEV_USER_ID_HEADER = "X-User-Id";

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 查询当前用户购物车
     */
    @GetMapping
    public Result<CartSummaryVO> getCart(
            @RequestHeader(DEV_USER_ID_HEADER)
            @Positive(message = "用户ID必须大于0")
            Long userId
    ) {
        return Result.success(
                shoppingCartService.getCart(userId)
        );
    }

    /**
     * 加入购物车
     */
    @PostMapping
    public Result<CartSummaryVO> addCartItem(
            @RequestHeader(DEV_USER_ID_HEADER)
            @Positive(message = "用户ID必须大于0")
            Long userId,

            @Valid @RequestBody AddCartItemDTO addCartItemDTO
    ) {
        return Result.success(
                shoppingCartService.addCartItem(
                        userId,
                        addCartItemDTO
                )
        );
    }

    /**
     * 修改购物车项数量
     */
    @PatchMapping("/{cartItemId}")
    public Result<CartSummaryVO> updateCartItemQuantity(
            @RequestHeader(DEV_USER_ID_HEADER)
            @Positive(message = "用户ID必须大于0")
            Long userId,

            @PathVariable
            @Positive(message = "购物车项ID必须大于0")
            Long cartItemId,

            @Valid @RequestBody
            UpdateCartItemQuantityDTO updateDTO
    ) {
        return Result.success(
                shoppingCartService.updateCartItemQuantity(
                        userId,
                        cartItemId,
                        updateDTO
                )
        );
    }

    /**
     * 删除一个购物车项
     */
    @DeleteMapping("/{cartItemId}")
    public Result<CartSummaryVO> deleteCartItem(
            @RequestHeader(DEV_USER_ID_HEADER)
            @Positive(message = "用户ID必须大于0")
            Long userId,

            @PathVariable
            @Positive(message = "购物车项ID必须大于0")
            Long cartItemId
    ) {
        return Result.success(
                shoppingCartService.deleteCartItem(
                        userId,
                        cartItemId
                )
        );
    }

    /**
     * 清空当前用户购物车
     */
    @DeleteMapping
    public Result<Void> clearCart(
            @RequestHeader(DEV_USER_ID_HEADER)
            @Positive(message = "用户ID必须大于0")
            Long userId
    ) {
        shoppingCartService.clearCart(userId);

        return Result.success();
    }

}
