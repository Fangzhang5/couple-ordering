package com.guowei.ordering.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCartItemDTO {

    @NotNull(message = "菜品ID不能为空")
    @Min(value = 1, message = "菜品ID必须大于0")
    private Long dishId;

    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量不能小于1")
    @Max(value = 99, message = "数量不能超过99")
    private Integer quantity;
}
