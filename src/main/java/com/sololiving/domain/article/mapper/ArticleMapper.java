package com.sololiving.domain.article.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.article.vo.ArticleVo;

@Mapper
public interface ArticleMapper {
    void insertArticle(ArticleVo article);

    void updateAllArticleScores();

    void updateViewCount(@Param("articleId") Long articleId, @Param("viewCount") int viewCount);

}
