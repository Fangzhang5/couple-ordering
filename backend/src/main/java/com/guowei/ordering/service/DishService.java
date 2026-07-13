package com.guowei.ordering.service;

import com.guowei.ordering.vo.DishVO;

import java.util.List;

public interface DishService {
    List<DishVO> listEnabledDishes(Long categoryId, String keyword);

    DishVO getEnabledDishById(Long id);
}
