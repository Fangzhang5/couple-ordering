package com.guowei.ordering.service;

import com.guowei.ordering.dto.CancelOrderDTO;
import com.guowei.ordering.dto.CreateOrderDTO;
import com.guowei.ordering.vo.OrderDetailVO;
import com.guowei.ordering.vo.OrderListItemVO;
import com.guowei.ordering.vo.PageResultVO;

public interface OrderService {

    OrderDetailVO createOrder(Long userId, CreateOrderDTO createOrderDTO);

    PageResultVO<OrderListItemVO> listOrders(Long userId, Integer page, Integer pageSize);

    OrderDetailVO getOrderDetail(Long userId, Long orderId);

    OrderDetailVO cancelOrder(Long userId, Long orderId, CancelOrderDTO cancelOrderDTO);

    OrderDetailVO completeOrder(Long userId, Long orderId);
}
