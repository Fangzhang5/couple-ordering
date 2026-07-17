package com.guowei.ordering.mapper;

import com.guowei.ordering.entity.Order;
import com.guowei.ordering.vo.OrderDetailVO;
import com.guowei.ordering.vo.OrderListItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    int insert(Order order);

    Order selectByIdAndUserId(
            @Param("id") Long id,
            @Param("userId") Long userId
    );

    OrderDetailVO selectDetailByIdAndUserId(
            @Param("id") Long id,
            @Param("userId") Long userId
    );

    Long countByUserId(
            @Param("userId") Long userId
    );

    List<OrderListItemVO> selectPageByUserId(
            @Param("userId") Long userId,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize
    );

    int cancelByIdAndUserId(
            @Param("id") Long id,
            @Param("userId") Long userId,
            @Param("cancelReason") String cancelReason
    );

    int completeByIdAndUserId(
            @Param("id") Long id,
            @Param("userId") Long userId
    );
}
