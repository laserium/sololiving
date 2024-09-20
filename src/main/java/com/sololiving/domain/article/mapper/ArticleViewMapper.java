package com.sololiving.domain.article.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.article.vo.ArticleVo;

@Mapper
public interface ArticleViewMapper {
    List<ArticleVo> getArticlesByScroll(@Param("offset") int offset, @Param("size") int size);

}
