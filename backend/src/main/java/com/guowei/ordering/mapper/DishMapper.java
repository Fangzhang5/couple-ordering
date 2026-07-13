package com.guowei.ordering.mapper;

import com.guowei.ordering.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishMapper {
    List<DishVO> selectEnabledList(@Param("categoryId") Long categoryId,
                                   @Param("keyword") String keyword);

    DishVO selectEnabledById(@Param("id") Long id);
}
