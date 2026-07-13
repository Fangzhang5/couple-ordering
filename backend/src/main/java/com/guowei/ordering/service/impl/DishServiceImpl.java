package com.guowei.ordering.service.impl;

import com.guowei.ordering.common.exception.BusinessException;
import com.guowei.ordering.mapper.DishMapper;
import com.guowei.ordering.service.DishService;
import com.guowei.ordering.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Override
    public List<DishVO> listEnabledDishes(Long categoryId, String keyword) {
        String normalizedKeyword = StringUtils.hasText(keyword) ? keyword.trim() : null;
        return dishMapper.selectEnabledList(categoryId, normalizedKeyword);
    }

    @Override
    public DishVO getEnabledDishById(Long id) {
        DishVO dish = dishMapper.selectEnabledById(id);
        if (dish == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, 40401, "菜品不存在或已下架");
        }
        return dish;
    }
}
