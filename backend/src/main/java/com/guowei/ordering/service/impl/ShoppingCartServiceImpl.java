package com.guowei.ordering.service.impl;

import com.guowei.ordering.common.exception.BusinessException;
import com.guowei.ordering.dto.AddCartItemDTO;
import com.guowei.ordering.dto.UpdateCartItemQuantityDTO;
import com.guowei.ordering.entity.ShoppingCart;
import com.guowei.ordering.mapper.DishMapper;
import com.guowei.ordering.mapper.ShoppingCartMapper;
import com.guowei.ordering.service.ShoppingCartService;
import com.guowei.ordering.vo.CartItemVO;
import com.guowei.ordering.vo.CartSummaryVO;
import com.guowei.ordering.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private static final int MAX_CART_ITEM_QUANTITY = 99;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 查询购物车并计算总数量、总金额
     */
    @Override
    public CartSummaryVO getCart(Long userId) {
        List<CartItemVO> items =
                shoppingCartMapper.selectCartItemsByUserId(userId);

        if (items == null) {
            items = Collections.emptyList();
        }

        int totalQuantity = items.stream()
                .map(CartItemVO::getQuantity)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();

        BigDecimal totalAmount = items.stream()
                .map(CartItemVO::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartSummaryVO.builder()
                .items(items)
                .totalQuantity(totalQuantity)
                .totalAmount(totalAmount)
                .build();
    }

    /**
     * 加入购物车
     */
    @Override
    @Transactional
    public CartSummaryVO addCartItem(Long userId, AddCartItemDTO addCartItemDTO) {
        Long dishId = addCartItemDTO.getDishId();
        Integer addQuantity = addCartItemDTO.getQuantity();

        // 1. 校验菜品是否存在并且处于上架状态
        DishVO dish = dishMapper.selectEnabledById(dishId);

        if (dish == null) {
            throw new BusinessException(
                    HttpStatus.NOT_FOUND,
                    40401,
                    "菜品不存在或已下架"
            );
        }

        // 2. 查询当前用户购物车中是否已经存在该菜品
        ShoppingCart existingCartItem =
                shoppingCartMapper.selectByUserIdAndDishId(
                        userId,
                        dishId
                );

        if (existingCartItem == null) {
            // 3. 第一次加入：插入新的购物车记录
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUserId(userId);
            shoppingCart.setDishId(dishId);
            shoppingCart.setQuantity(addQuantity);

            shoppingCartMapper.insert(shoppingCart);
        } else {
            // 4. 已经存在：累加数量
            int newQuantity =
                    existingCartItem.getQuantity() + addQuantity;

            if (newQuantity > MAX_CART_ITEM_QUANTITY) {
                throw new BusinessException(
                        HttpStatus.CONFLICT,
                        40901,
                        "购物车菜品数量不能超过99"
                );
            }

            shoppingCartMapper.updateQuantityByIdAndUserId(
                    existingCartItem.getId(),
                    userId,
                    newQuantity
            );
        }

        // 5. 返回更新后的完整购物车
        return getCart(userId);
    }

    /**
     * 修改购物车项数量
     */
    @Override
    @Transactional
    public CartSummaryVO updateCartItemQuantity(Long userId, Long cartItemId, UpdateCartItemQuantityDTO updateDTO) {
        int affectedRows =
                shoppingCartMapper.updateQuantityByIdAndUserId(
                        cartItemId,
                        userId,
                        updateDTO.getQuantity()
                );

        if (affectedRows == 0) {
            throw new BusinessException(
                    HttpStatus.NOT_FOUND,
                    40402,
                    "购物车项不存在"
            );
        }

        return getCart(userId);
    }

    /**
     * 删除一个购物车项
     */
    @Override
    @Transactional
    public CartSummaryVO deleteCartItem(Long userId, Long cartItemId) {
        int affectedRows =
                shoppingCartMapper.deleteByIdAndUserId(
                        cartItemId,
                        userId
                );

        if (affectedRows == 0) {
            throw new BusinessException(
                    HttpStatus.NOT_FOUND,
                    40402,
                    "购物车项不存在"
            );
        }

        return getCart(userId);
    }

    /**
     * 清空购物车
     */
    @Override
    @Transactional
    public void clearCart(Long userId) {
        shoppingCartMapper.deleteByUserId(userId);
    }
}
