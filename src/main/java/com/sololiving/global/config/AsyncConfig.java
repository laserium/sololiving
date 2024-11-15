package com.sololiving.global.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    // 로그 출력용 스레드 풀 구성
    @Bean(name = "logTaskExecutor")
    public Executor logTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // 스레드 풀의 기본 크기
        executor.setMaxPoolSize(5); // 최대 스레드 수
        executor.setQueueCapacity(100); // 큐 대기열 크기
        executor.setThreadNamePrefix("LogThread-"); // 스레드 이름 접두사
        executor.initialize();
        return executor;
    }

    // 이메일 발송용 스레드 풀 구성
    @Bean(name = "emailTaskExecutor")
    public Executor emailTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // 스레드 풀의 기본 크기
        executor.setMaxPoolSize(5); // 최대 스레드 수
        executor.setQueueCapacity(100); // 큐 대기열 크기
        executor.setThreadNamePrefix("EmailThread-"); // 스레드 이름 접두사
        executor.initialize();
        return executor;
    }

    // 문자 발송용 스레드 풀 구성
    @Bean(name = "smsTaskExecutor")
    public Executor smsTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("SmsThread-");
        executor.initialize();
        return executor;
    }

    // CHATGPT API 자동 응답용 스레드 풀 구성
    @Bean(name = "AiCommentTaskExecutor")
    public Executor AICommentTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AiThread-");
        executor.initialize();
        return executor;
    }

    // 사용자 행동 로그용 스레드 풀 구성
    @Bean(name = "userLogTaskExecutor")
    public Executor userLogTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("userLogThread-");
        executor.initialize();
        return executor;
    }
}
