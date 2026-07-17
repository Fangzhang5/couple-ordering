package com.guowei.ordering.service.impl;

import com.guowei.ordering.common.exception.BusinessException;
import com.guowei.ordering.dto.CancelOrderDTO;
import com.guowei.ordering.dto.CreateOrderDTO;
import com.guowei.ordering.entity.Order;
import com.guowei.ordering.entity.OrderItem;
import com.guowei.ordering.mapper.OrderItemMapper;
import com.guowei.ordering.mapper.OrderMapper;
import com.guowei.ordering.service.OrderService;
import com.guowei.ordering.service.ShoppingCartService;
import com.guowei.ordering.vo.CartItemVO;
import com.guowei.ordering.vo.CartSummaryVO;
import com.guowei.ordering.vo.OrderDetailVO;
import com.guowei.ordering.vo.OrderItemVO;
import com.guowei.ordering.vo.OrderListItemVO;
import com.guowei.ordering.vo.PageResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderServiceImpl implements OrderService {

    private static final int STATUS_PENDING = 1;
    private static final int STATUS_COMPLETED = 4;
    private static final int STATUS_CANCELED = 5;
    private static final DateTimeFormatter ORDER_NUMBER_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public OrderDetailVO createOrder(Long userId, CreateOrderDTO createOrderDTO) {
        CartSummaryVO cart = shoppingCartService.getCart(userId);

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BusinessException(
                    HttpStatus.CONFLICT,
                    40902,
                    "shopping cart is empty"
            );
        }

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUserId(userId);
        order.setStatus(STATUS_PENDING);
        order.setAmount(cart.getTotalAmount());
        order.setRemark(normalizeBlank(createOrderDTO.getRemark()));
        order.setExpectedTime(createOrderDTO.getExpectedTime());

        orderMapper.insert(order);

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> buildOrderItem(order.getId(), cartItem))
                .toList();

        orderItemMapper.batchInsert(orderItems);
        shoppingCartService.clearCart(userId);

        return getOrderDetail(userId, order.getId());
    }

    @Override
    public PageResultVO<OrderListItemVO> listOrders(Long userId, Integer page, Integer pageSize) {
        int offset = (page - 1) * pageSize;
        long total = orderMapper.countByUserId(userId);
        List<OrderListItemVO> records = orderMapper.selectPageByUserId(
                userId,
                offset,
                pageSize
        );

        return PageResultVO.<OrderListItemVO>builder()
                .page(page)
                .pageSize(pageSize)
                .total(total)
                .records(records)
                .build();
    }

    @Override
    public OrderDetailVO getOrderDetail(Long userId, Long orderId) {
        OrderDetailVO orderDetail = orderMapper.selectDetailByIdAndUserId(orderId, userId);

        if (orderDetail == null) {
            throw new BusinessException(
                    HttpStatus.NOT_FOUND,
                    40403,
                    "order not found"
            );
        }

        List<OrderItemVO> items = orderItemMapper.selectByOrderId(orderId);
        orderDetail.setItems(items);
        orderDetail.setTotalQuantity(calculateTotalQuantity(items));

        return orderDetail;
    }

    @Override
    @Transactional
    public OrderDetailVO cancelOrder(Long userId, Long orderId, CancelOrderDTO cancelOrderDTO) {
        Order order = getExistingOrder(userId, orderId);

        if (Objects.equals(order.getStatus(), STATUS_COMPLETED)) {
            throw new BusinessException(
                    HttpStatus.CONFLICT,
                    40903,
                    "completed order cannot be canceled"
            );
        }

        if (Objects.equals(order.getStatus(), STATUS_CANCELED)) {
            throw new BusinessException(
                    HttpStatus.CONFLICT,
                    40904,
                    "order has already been canceled"
            );
        }

        orderMapper.cancelByIdAndUserId(
                orderId,
                userId,
                normalizeBlank(cancelOrderDTO.getCancelReason())
        );

        return getOrderDetail(userId, orderId);
    }

    @Override
    @Transactional
    public OrderDetailVO completeOrder(Long userId, Long orderId) {
        Order order = getExistingOrder(userId, orderId);

        if (Objects.equals(order.getStatus(), STATUS_CANCELED)) {
            throw new BusinessException(
                    HttpStatus.CONFLICT,
                    40905,
                    "canceled order cannot be completed"
            );
        }

        if (!Objects.equals(order.getStatus(), STATUS_COMPLETED)) {
            orderMapper.completeByIdAndUserId(orderId, userId);
        }

        return getOrderDetail(userId, orderId);
    }

    private Order getExistingOrder(Long userId, Long orderId) {
        Order order = orderMapper.selectByIdAndUserId(orderId, userId);

        if (order == null) {
            throw new BusinessException(
                    HttpStatus.NOT_FOUND,
                    40403,
                    "order not found"
            );
        }

        return order;
    }

    private OrderItem buildOrderItem(Long orderId, CartItemVO cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderId);
        orderItem.setDishId(cartItem.getDishId());
        orderItem.setDishName(cartItem.getDishName());
        orderItem.setDishImageUrl(cartItem.getDishImageUrl());
        orderItem.setDishPrice(cartItem.getDishPrice());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setAmount(cartItem.getAmount());
        return orderItem;
    }

    private Integer calculateTotalQuantity(List<OrderItemVO> items) {
        if (items == null) {
            return 0;
        }

        return items.stream()
                .map(OrderItemVO::getQuantity)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(ORDER_NUMBER_TIME_FORMATTER);
        int random = ThreadLocalRandom.current().nextInt(1000, 10000);
        return timestamp + random;
    }

    private String normalizeBlank(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return value.trim();
    }
}
