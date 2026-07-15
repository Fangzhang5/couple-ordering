package com.guowei.ordering.mapper;

import com.guowei.ordering.entity.ShoppingCart;
import com.guowei.ordering.vo.CartItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 查询当前用户的购物车明细
     */
    List<CartItemVO> selectCartItemsByUserId(
            @Param("userId") Long userId
    );

    /**
     * 根据用户ID和菜品ID查询购物车项
     */
    ShoppingCart selectByUserIdAndDishId(
            @Param("userId") Long userId,
            @Param("dishId") Long dishId
    );

    /**
     * 新增购物车项
     */
    int insert(ShoppingCart shoppingCart);

    /**
     * 修改购物车项数量
     */
    int updateQuantityByIdAndUserId(
            @Param("id") Long id,
            @Param("userId") Long userId,
            @Param("quantity") Integer quantity
    );

    /**
     * 删除一个购物车项
     */
    int deleteByIdAndUserId(
            @Param("id") Long id,
            @Param("userId") Long userId
    );

    /**
     * 清空当前用户购物车
     */
    int deleteByUserId(
            @Param("userId") Long userId
    );
}
