package com.guowei.ordering.service.impl;

import com.guowei.ordering.mapper.CategoryMapper;
import com.guowei.ordering.service.CategoryService;
import com.guowei.ordering.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryVO> listEnabledCategories() {
        return categoryMapper.selectEnabledList();
    }
}
