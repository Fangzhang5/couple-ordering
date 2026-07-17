package com.guowei.ordering.mapper;

import com.guowei.ordering.entity.OrderItem;
import com.guowei.ordering.vo.OrderItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    int batchInsert(@Param("items") List<OrderItem> items);

    List<OrderItemVO> selectByOrderId(@Param("orderId") Long orderId);
}
