package com.sololiving.domain.article.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.article.dto.request.CreateArticleRequestDto;
import com.sololiving.domain.article.enums.Status;
import com.sololiving.domain.article.mapper.ArticleMapper;
import com.sololiving.domain.article.vo.ArticleVo;
import com.sololiving.domain.media.service.MediaUploadService;
import com.sololiving.global.util.aws.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final S3Uploader s3Uploader;
    private final ArticleMapper articleMapper;
    private final MediaUploadService mediaService;

    public void createArticle(CreateArticleRequestDto requestDto, String userId, List<String> tempMediaUrls) {
        // 게시글 저장
        ArticleVo article = buildArticle(requestDto, userId);
        articleMapper.insertArticle(article);

        // 게시글이 작성된 후 mediaList가 있으면 파일 이동 및 DB 저장
        if (tempMediaUrls != null && !tempMediaUrls.isEmpty()) {
            mediaService.attachFilesToArticle(article.getArticleId(), tempMediaUrls);
        }
    }

    // ArticleVo 빌더 로직을 메서드로 분리
    private ArticleVo buildArticle(CreateArticleRequestDto requestDto, String userId) {
        return ArticleVo.builder()
                .writer(userId)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .categoryId(requestDto.getCategoryId())
                .build();
    }

}
