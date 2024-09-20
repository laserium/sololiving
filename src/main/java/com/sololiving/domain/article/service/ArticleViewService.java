package com.sololiving.domain.article.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sololiving.domain.article.mapper.ArticleViewMapper;
import com.sololiving.domain.article.vo.ArticleVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleViewService {

    private final ArticleViewMapper articleViewMapper;

    // 페이징을 통해 특정 페이지의 게시글을 가져오는 메소드
    public List<ArticleVo> getArticlesByScroll(int page, int size) {
        int offset = page * size; // 페이지에 따라 조회할 오프셋 계산
        return articleViewMapper.getArticlesByScroll(offset, size);
    }
}
