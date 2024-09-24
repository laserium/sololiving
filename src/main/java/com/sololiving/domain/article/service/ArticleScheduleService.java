package com.sololiving.domain.article.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sololiving.domain.article.mapper.ArticleMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleScheduleService {

    private final ArticleMapper articleMapper;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateArticleScores() {
        log.info("[SCHEDULED] : ArticleScheduleService.updateArticleScores() => START");
        try {
            articleMapper.updateAllArticleScores();
            log.info("[SCHEDULED] : ArticleScheduleService.updateArticleScores() => SUCCESS");
        } catch (Exception e) {
            log.error("[SCHEDULED] : ArticleScheduleService.updateArticleScores() => FAIL : ", e);
        }
    }

}
