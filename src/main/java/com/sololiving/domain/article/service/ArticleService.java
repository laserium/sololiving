package com.sololiving.domain.article.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.sololiving.domain.article.dto.request.CreateArticleRequestDto;
import com.sololiving.domain.article.dto.request.UpdateArticleRequestDto;
import com.sololiving.domain.article.dto.response.CreateArticleResponseDto;
import com.sololiving.domain.article.exception.ArticleErrorCode;
import com.sololiving.domain.article.mapper.ArticleMapper;
import com.sololiving.domain.article.vo.ArticleVo;
import com.sololiving.domain.comment.mapper.CommentMapper;
import com.sololiving.domain.comment.vo.CommentVo;
import com.sololiving.domain.media.mapper.MediaMapper;
import com.sololiving.domain.media.service.MediaService;
import com.sololiving.domain.media.service.MediaUploadService;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.util.aws.S3Uploader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleService {

    private final WebClient.Builder webClientBuilder;
    private static final String AI_CATEGORY_NAME = "ai";

    @Value("${chatgpt.api.key}")
    private String chatGptApiKey;

    private final RedisTemplate<String, String> redisTemplate;
    private final ArticleMapper articleMapper;
    private final MediaMapper mediaMapper;
    private final MediaUploadService mediaUploadService;
    private final MediaService mediaService;
    private final S3Uploader s3Uploader;
    private final CommentMapper commentMapper;

    public CreateArticleResponseDto createArticle(CreateArticleRequestDto requestDto, String userId,
            List<String> tempMediaUrls) {
        if (requestDto == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        CreateArticleResponseDto responseDto = addArticle(requestDto, userId, tempMediaUrls);
        if (requestDto.getCategoryCode().equals(AI_CATEGORY_NAME)) {
            generateAIComment(requestDto.getContent(), responseDto.getArticleId());
        }
        return responseDto;
    }

    // CHAT GPT 자동 답변
    @Async("AiCommentTaskExecutor")
    private void generateAIComment(String content, Long articleId) {
        String aiComment = callAiApi(content);
        insertAIComment(aiComment, articleId);
    }

    @Transactional
    private void insertAIComment(String aiComment, Long articleId) {
        CommentVo aiCommentVo = CommentVo.builder()
                .articleId(articleId)
                .parentCommentId(null)
                .writer("Smart_AI_Bot")
                .content(aiComment)
                .build();

        commentMapper.insertComment(aiCommentVo);
    }

    private String callAiApi(String prompt) {
        String url = "https://api.openai.com/v1/chat/completions";

        // requestBody 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "ft:gpt-4o-mini-2024-07-18:personal::ASKjlLbb");

        // 'messages' 필드에 사용자의 메시지를 넣기 위해 List 생성
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);

        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 500);
        requestBody.put("temperature", 0.7);

        return webClientBuilder.build()
                .post()
                .uri(url)
                .header("Authorization", "Bearer " + chatGptApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(responseBody -> {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                })
                .block();
    }

    // 게시글 작성
    @Transactional
    private CreateArticleResponseDto addArticle(CreateArticleRequestDto requestDto, String userId,
            List<String> tempMediaUrls) {
        ArticleVo articleVo = buildArticle(requestDto, userId);
        articleMapper.insertArticle(articleVo);

        // 미디어 파일 처리 및 mediaTypeBitmask 결정
        if (tempMediaUrls != null && !tempMediaUrls.isEmpty()) {
            int mediaTypeBitmask = mediaUploadService.attachFilesToArticle(articleVo.getArticleId(), tempMediaUrls);
            articleMapper.updateMediaType(articleVo.getArticleId(), mediaTypeBitmask);
        }
        return CreateArticleResponseDto.builder().articleId(articleVo.getArticleId()).build();
    }

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
        List<String> mediaUrls = mediaMapper.selectMediaUrlsByArticleId(articleId);
        if (mediaUrls != null && !mediaUrls.isEmpty()) {
            // S3에서 파일 삭제
            for (String mediaUrl : mediaUrls) {
                try {
                    s3Uploader.deleteS3(mediaUrl);
                } catch (Exception e) {
                    log.error("Failed to delete media from S3 for mediaUrl: {}", mediaUrl, e);
                }
            }
            // DB에서 미디어 정보 삭제
            mediaMapper.deleteMediaUrlsByArticleId(articleId);
        }

        // 3. 게시글 정보 초기화 (삭제된 게시글 처리)
        articleMapper.updateArticleAsDeleted(articleId);

    }

    // 작성자 검증
    public void validateWriter(Long articleId, String userId) {
        if (!articleMapper.verifyArticleWriter(articleId, userId)) {
            throw new ErrorException(ArticleErrorCode.VERIFY_WRITER_FAILED);
        }
    }

    // 게시글 조회수 증가
    public void incrementArticleViewCount(Long articleId) {
        redisTemplate.opsForValue().increment("ARTICLE:" + articleId + ":view_cnt");
    }

}
