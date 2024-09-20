package com.sololiving.domain.category.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.category.vo.CategoryVo;

@Mapper
public interface CategoryMapper {
    // CategoryVo findCategoryById(@Param("categoryId") Long categoryId);
}
