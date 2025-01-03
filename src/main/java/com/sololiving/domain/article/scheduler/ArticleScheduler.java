package com.sololiving.domain.article.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.connection.RedisKeyCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
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

    // 게시글의 고유 점수 스케쥴링 : 자정 마다
    @Scheduled(cron = "0 0 0 1 * *")
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

        // Redis에서 조회수 관련 키 가져오기
        List<String> keys = scanKeys("ARTICLE:*:view_cnt");

        if (keys.isEmpty()) {
            log.info("[SCHEDULED] : 조회할 키가 없습니다.");
            return;
        }

        // 각 키에 대한 처리
        keys.forEach(this::processViewCountKey);
    }

    // SCAN 명령을 사용하여 키를 검색하는 메서드
    public List<String> scanKeys(String pattern) {
        List<String> keys = new ArrayList<>();

        redisTemplate.execute((RedisCallback<Void>) connection -> {
            RedisKeyCommands keyCommands = connection.keyCommands();

            // ScanOptions 사용
            ScanOptions options = ScanOptions.scanOptions()
                    .match(pattern)
                    .count(100)
                    .build();

            try (var cursor = keyCommands.scan(options)) {
                cursor.forEachRemaining(key -> keys.add(new String(key)));
            } catch (Exception e) {
                log.error("[SCHEDULED] : Redis에서 키 검색 중 오류 발생", e);
            }
            return null;
        });

        return keys;
    }

    private void processViewCountKey(String key) {
        // Key에서 articleId 추출 (예: "ARTICLE:123:view_cnt" -> 123)
        String[] parts = key.split(":");

        // 키 형식이 예상과 일치하는지 확인
        if (parts.length != 3 || !"ARTICLE".equals(parts[0]) || !"view_cnt".equals(parts[2])) {
            log.warn("[SCHEDULED] : 잘못된 키 형식 발견 - key: {}", key);
            return;
        }

        Long articleId = parseArticleId(parts[1]);
        if (articleId == null) {
            log.error("[SCHEDULED] : articleId 변환 오류 발생 - key: {}", key);
            return;
        }

        // Redis에서 조회수 가져오기 (조회수는 자동으로 Redis에 누적되며, Redis의 INCR 명령을 사용)
        Long viewCount = redisTemplate.opsForValue().increment(key, 0); // 현재 Redis에 저장된 조회수를 가져옵니다 (누적 값)

        if (viewCount == null) {
            log.warn("[SCHEDULED] : Redis에서 조회수를 가져오지 못했습니다. key: {}", key);
            return;
        }

        // DB에 조회수 업데이트
        articleMapper.updateViewCount(articleId, viewCount.intValue());

        // Redis에서 해당 조회수 키 삭제 (옵션: 삭제할지 유지할지 결정)
        redisTemplate.delete(key);
        log.info("[SCHEDULED] : 게시글 ID {} 의 조회 수 {} 를 DB에 업데이트 완료", articleId, viewCount);
    }

    private Long parseArticleId(String articleIdStr) {
        try {
            return Long.parseLong(articleIdStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
