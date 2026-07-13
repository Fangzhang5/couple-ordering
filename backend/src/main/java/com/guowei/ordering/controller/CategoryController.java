package com.guowei.ordering.controller;

import com.guowei.ordering.common.result.Result;
import com.guowei.ordering.service.CategoryService;
import com.guowei.ordering.vo.CategoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/categories")
    public Result<List<CategoryVO>> listEnabledCategories(){
        log.info("查询已启用的分类");
        return Result.success(categoryService.listEnabledCategories());
    }
}
