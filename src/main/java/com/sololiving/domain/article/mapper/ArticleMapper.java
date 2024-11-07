package com.sololiving.domain.article.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.article.vo.ArticleVo;

@Mapper
public interface ArticleMapper {

    // 게시글 작성
    void insertArticle(ArticleVo articleVo);

    // 게시글의 고유 점수 스케쥴링
    void updateAllArticleScores();

    // 게시글 조회 수 업데이트 스케쥴링
    void updateViewCount(@Param("articleId") Long articleId, @Param("viewCount") int viewCount);

    // 작성자 검증
    boolean verifyArticleWriter(@Param("articleId") Long articleId, @Param("userId") String userId);

    // 게시글 찾기
    ArticleVo selectByArticleId(@Param("articleId") Long articleId);

    // 게시글 수정
    void updateArticle(ArticleVo articleVo);

    // 게시글 삭제 - 게시글 상태를 삭제로 변경 UPDATE
    void updateArticleAsDeleted(@Param("articleId") Long articleId);

    // 게시글 아이디로 게시글 존재 유무 확인
    boolean checkArticleExists(@Param("articleId") Long articleId);

    // 게시글 추천 수 업데이트
    void updateArticleLikeCount(@Param("articleId") Long articleId);
}
