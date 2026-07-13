package com.guowei.ordering.mapper;

import com.guowei.ordering.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<CategoryVO> selectEnabledList();
}
