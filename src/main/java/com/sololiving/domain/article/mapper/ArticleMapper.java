package com.sololiving.domain.article.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.sololiving.domain.article.vo.ArticleVo;

@Mapper
public interface ArticleMapper {
    void insertArticle(ArticleVo article);

    void updateAllArticleScores();

}
