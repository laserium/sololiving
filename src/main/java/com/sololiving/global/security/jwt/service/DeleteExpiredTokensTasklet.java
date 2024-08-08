// package com.sololiving.global.security.jwt.service;

// import org.springframework.batch.core.StepContribution;
// import org.springframework.batch.core.scope.context.ChunkContext;
// import org.springframework.batch.core.step.tasklet.Tasklet;
// import org.springframework.batch.repeat.RepeatStatus;
// import org.springframework.stereotype.Service;

// import com.sololiving.global.security.jwt.mapper.RefreshTokenMapper;

// import lombok.RequiredArgsConstructor;

// import java.time.LocalDateTime;

// @Service
// @RequiredArgsConstructor
// public class DeleteExpiredTokensTasklet implements Tasklet {

// private final RefreshTokenMapper refreshTokenMapper;

// @Override
// public RepeatStatus execute(StepContribution contribution, ChunkContext
// chunkContext) throws Exception {
// LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
// int deletedRows = refreshTokenMapper.deleteExpiredTokens(thirtyDaysAgo);
// System.out.println("Deleted rows: " + deletedRows);
// return RepeatStatus.FINISHED;
// }
// }
