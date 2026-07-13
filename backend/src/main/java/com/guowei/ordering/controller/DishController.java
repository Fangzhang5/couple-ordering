package com.guowei.ordering.controller;

import com.guowei.ordering.common.result.Result;
import com.guowei.ordering.service.DishService;
import com.guowei.ordering.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.channels.Pipe;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dishes")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping
    public Result<List<DishVO>> listEnabledDishes(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        return Result.success(dishService.listEnabledDishes(categoryId, keyword));
    }

    @GetMapping("/{id}")
    public Result<DishVO> getEnabledDishById(@PathVariable Long id) {
        return Result.success(dishService.getEnabledDishById(id));
    }
}
