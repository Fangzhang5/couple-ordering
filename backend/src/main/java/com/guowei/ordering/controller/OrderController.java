package com.guowei.ordering.controller;

import com.guowei.ordering.common.result.Result;
import com.guowei.ordering.dto.CancelOrderDTO;
import com.guowei.ordering.dto.CreateOrderDTO;
import com.guowei.ordering.service.OrderService;
import com.guowei.ordering.vo.OrderDetailVO;
import com.guowei.ordering.vo.OrderListItemVO;
import com.guowei.ordering.vo.PageResultVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@Validated
public class OrderController {

    private static final String DEV_USER_ID_HEADER = "X-User-Id";

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Result<OrderDetailVO> createOrder(
            @RequestHeader(DEV_USER_ID_HEADER)
            @Positive(message = "user id must be greater than 0")
            Long userId,

            @Valid @RequestBody CreateOrderDTO createOrderDTO
    ) {
        return Result.success(
                orderService.createOrder(userId, createOrderDTO)
        );
    }

    @GetMapping
    public Result<PageResultVO<OrderListItemVO>> listOrders(
            @RequestHeader(DEV_USER_ID_HEADER)
            @Positive(message = "user id must be greater than 0")
            Long userId,

            @RequestParam(defaultValue = "1")
            @Positive(message = "page must be greater than 0")
            Integer page,

            @RequestParam(defaultValue = "10")
            @Positive(message = "pageSize must be greater than 0")
            @Max(value = 100, message = "pageSize must not exceed 100")
            Integer pageSize
    ) {
        return Result.success(orderService.listOrders(userId, page, pageSize));
    }

    @GetMapping("/{orderId}")
    public Result<OrderDetailVO> getOrderDetail(
            @RequestHeader(DEV_USER_ID_HEADER)
            @Positive(message = "user id must be greater than 0")
            Long userId,

            @PathVariable
            @Positive(message = "order id must be greater than 0")
            Long orderId
    ) {
        return Result.success(
                orderService.getOrderDetail(userId, orderId)
        );
    }

    @PatchMapping("/{orderId}/cancel")
    public Result<OrderDetailVO> cancelOrder(
            @RequestHeader(DEV_USER_ID_HEADER)
            @Positive(message = "user id must be greater than 0")
            Long userId,

            @PathVariable
            @Positive(message = "order id must be greater than 0")
            Long orderId,

            @Valid @RequestBody CancelOrderDTO cancelOrderDTO
    ) {
        return Result.success(
                orderService.cancelOrder(userId, orderId, cancelOrderDTO)
        );
    }

    @PatchMapping("/{orderId}/complete")
    public Result<OrderDetailVO> completeOrder(
            @RequestHeader(DEV_USER_ID_HEADER)
            @Positive(message = "user id must be greater than 0")
            Long userId,

            @PathVariable
            @Positive(message = "order id must be greater than 0")
            Long orderId
    ) {
        return Result.success(
                orderService.completeOrder(userId, orderId)
        );
    }
}
