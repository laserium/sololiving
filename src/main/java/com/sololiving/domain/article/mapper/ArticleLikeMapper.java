package com.sololiving.domain.article.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.article.vo.ArticleLikeVo;

@Mapper
public interface ArticleLikeMapper {
    // 댓글 추천 추가
    void insertArticleLike(ArticleLikeVo articleLikeVo);

    // 이미 추천한 댓글에 재추천 불가
    boolean hasUserLikedArticle(@Param("articleId") Long articleId, @Param("userId") String userId);

    // 댓글 추천 취소
    void deleteArticleLike(@Param("articleId") Long articleId, @Param("userId") String userId);

}
