package com.guowei.ordering.service;

import com.guowei.ordering.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
    List<CategoryVO> listEnabledCategories();
}
