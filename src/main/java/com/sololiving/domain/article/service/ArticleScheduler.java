package com.sololiving.domain.article.service;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sololiving.domain.article.mapper.ArticleMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ArticleScheduler {
    private final RedisTemplate<String, String> redisTemplate;

    private final ArticleMapper articleMapper;

    // 게시글의 고유 점수 스케쥴링 : 3시간 마다
    @Scheduled(cron = "0 0 0/3 * * *")
    public void updateArticleScores() {
        log.info("[SCHEDULED] : 게시글의 고유 점수 스케쥴링 => START");
        try {
            articleMapper.updateAllArticleScores();
            log.info("[SCHEDULED] : 게시글의 고유 점수 스케쥴링 => SUCCESS");
        } catch (Exception e) {
            log.error("[SCHEDULED] : 게시글의 고유 점수 스케쥴링 => FAIL : ", e);
        }
    }

    // 게시글 조회 수 업데이트 스케쥴링 : 1분 마다
    @Scheduled(fixedRate = 60000)
    public void syncArticleViewsToDb() {
        log.info("[SCHEDULED] : 게시글 조회 수 업데이트 스케쥴링 => START");
        try {
            Set<String> keys = redisTemplate.keys("ARTICLE:*:view_cnt");

            if (keys != null && !keys.isEmpty()) {
                for (String key : keys) {
                    // Key에서 articleId 추출 (예: "article:view:123" -> 123)
                    Long articleId = Long.parseLong(key.split(":")[2]);

                    // Redis에서 조회수 가져오기
                    String viewCountStr = redisTemplate.opsForValue().get(key);
                    if (viewCountStr != null) {
                        int viewCount = Integer.parseInt(viewCountStr);

                        // DB에 조회수 업데이트
                        articleMapper.updateViewCount(articleId, viewCount);

                        // Redis에서 해당 조회수 키 삭제 (옵션: 삭제할지 유지할지 결정)
                        redisTemplate.delete(key);
                        log.info("[SCHEDULED] : 게시글 조회 수 업데이트 스케쥴링 => SUCCESS");
                    }
                }
            }
        } catch (Exception e) {
            log.error("[SCHEDULED] : 게시글 조회 수 업데이트 스케쥴링 => START : ", e);
        }

    }

}
