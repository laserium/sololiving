package com.sololiving.domain.article.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ArticleCreatedEvent extends ApplicationEvent {
    private final Long articleId;
    private final String content;

    public ArticleCreatedEvent(Object source, Long articleId, String content) {
        super(source);
        this.articleId = articleId;
        this.content = content;
    }
}
