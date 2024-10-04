package com.sololiving.domain.article.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.article.dto.request.CreateArticleRequestDto;
import com.sololiving.domain.article.dto.request.UpdateArticleRequestDto;
import com.sololiving.domain.article.dto.response.CreateArticleResponseDto;
import com.sololiving.domain.article.exception.ArticleErrorCode;
import com.sololiving.domain.article.mapper.ArticleMapper;
import com.sololiving.domain.article.vo.ArticleVo;
import com.sololiving.domain.media.mapper.MediaMapper;
import com.sololiving.domain.media.service.MediaService;
import com.sololiving.domain.media.service.MediaUploadService;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleMapper articleMapper;
    private final MediaMapper mediaMapper;
    private final MediaUploadService mediaUploadService;
    private final MediaService mediaService;

    // 게시글 작성
    @Transactional
    public CreateArticleResponseDto addArticle(CreateArticleRequestDto requestDto, String userId,
            List<String> tempMediaUrls) {
        // 게시글 저장
        ArticleVo articleVo = buildArticle(requestDto, userId);
        articleMapper.insertArticle(articleVo);

        // 게시글이 작성된 후 mediaList가 있으면 파일 이동 및 DB 저장
        if (tempMediaUrls != null && !tempMediaUrls.isEmpty()) {
            mediaUploadService.attachFilesToArticle(articleVo.getArticleId(), tempMediaUrls);
        }
        return CreateArticleResponseDto.builder().articleId(articleVo.getArticleId()).build();
    }

    // ArticleVo 빌더 로직을 메서드로 분리
    private ArticleVo buildArticle(CreateArticleRequestDto requestDto, String userId) {
        return ArticleVo.builder()
                .writer(userId)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .categoryCode(requestDto.getCategoryCode())
                .build();
    }

    // 게시글 수정
    @Transactional
    public void modifyArticle(UpdateArticleRequestDto requestDto, Long articleId, String userId) {
        ArticleVo articleVo = articleMapper.selectByArticleId(articleId);
        if (articleVo == null) {
            throw new ErrorException(ArticleErrorCode.ARTICLE_NOT_FOUND);
        }
        articleVo = articleVo.toBuilder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .categoryCode(requestDto.getCategoryCode())
                .build();
        articleMapper.updateArticle(articleVo);

        // 미디어 파일들도 저장
        mediaService.updateMediaInArticle(articleId, requestDto.getUpdatedMediaUrls());

    }

    // 게시글 삭제
    @Transactional
    public void removeArticle(Long articleId) {

        // 1. 미디어 파일 조회 및 삭제
        // List<String> mediaUrls = mediaMapper.selectMediaUrlsByArticleId(articleId);
        // if (mediaUrls != null && !mediaUrls.isEmpty()) {
        // // S3에서 파일 삭제
        // for (String mediaUrl : mediaUrls) {
        // try {
        // s3Uploader.deleteS3(mediaUrl);
        // } catch (Exception e) {
        // log.error("Failed to delete media from S3 for mediaUrl: {}", mediaUrl, e);
        // }
        // }
        // // DB에서 미디어 정보 삭제
        // mediaMapper.deleteMediaByArticleId(articleId);
        // }

        // // 3. 게시글 정보 초기화 (삭제된 게시글 처리)
        // articleMapper.updateArticleAsDeleted(articleId);
        // final String DELETED_TITLE = "삭제된 게시글입니다";
        // final String DELETED_CONTENT = "삭제된 게시글입니다";

    }

    // 작성자 검증
    public void validateWriter(Long articleId, String userId) {
        if (!articleMapper.verifyArticleWriter(articleId, userId)) {
            throw new ErrorException(ArticleErrorCode.VERIFY_WRITER_FAILED);
        }
    }

}
