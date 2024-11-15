package com.sololiving.domain.log.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sololiving.domain.log.enums.ActivityType;
import com.sololiving.domain.log.enums.AuthMethod;
import com.sololiving.domain.log.enums.BlockMethod;
import com.sololiving.domain.log.enums.BoardMethod;
import com.sololiving.domain.log.enums.FollowMethod;
import com.sololiving.domain.log.mapper.UserActivityLogMapper;
import com.sololiving.domain.log.vo.UserActivityLogVo;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserActivityLogService {

        private final UserActivityLogMapper userActivityLogMapper;
        private final UserAuthService userAuthService;

        // AUTH
        @Async("userLogTaskExecutor")
        public void insertAuthLog(String userId, String ipAddress, AuthMethod authMethod) {
                UserActivityLogVo userActivityLogVo = UserActivityLogVo.builder()
                                .userId(userId)
                                .ipAddress(ipAddress)
                                .activityType(ActivityType.AUTH)
                                .build();

                userActivityLogMapper
                                .insertActivityLog(userActivityLogVo);
                userActivityLogMapper.insertAuthLog(userActivityLogVo.getId(), authMethod);
        }

        // ARTICLE
        @Async("userLogTaskExecutor")
        public void insertArticleLog(String userId, String ipAddress, Long articleId, BoardMethod boardMethod) {
                UserActivityLogVo userActivityLogVo = UserActivityLogVo.builder()
                                .userId(userId)
                                .ipAddress(ipAddress)
                                .activityType(ActivityType.ARTICLE)
                                .build();
                userActivityLogMapper
                                .insertActivityLog(userActivityLogVo);
                userActivityLogMapper.insertArticleLog(userActivityLogVo.getId(), articleId, boardMethod);
        }

        // COMMENT
        // @Async("userLogTaskExecutor")
        public void insertCommentLog(String userId, String ipAddress, Long commentId, BoardMethod boardMethod) {
                UserActivityLogVo userActivityLogVo = UserActivityLogVo.builder()
                                .userId(userId)
                                .ipAddress(ipAddress)
                                .activityType(ActivityType.COMMENT)
                                .build();
                userActivityLogMapper
                                .insertActivityLog(userActivityLogVo);
                userActivityLogMapper.insertCommentLog(userActivityLogVo.getId(), commentId, boardMethod);
        }

        // FOLLOW
        @Async("userLogTaskExecutor")
        public void insertFollowLog(String userId, String ipAddress, String targetId, FollowMethod followMethod) {
                UserActivityLogVo userActivityLogVo = UserActivityLogVo.builder()
                                .userId(userId)
                                .ipAddress(ipAddress)
                                .activityType(ActivityType.FOLLOW)
                                .build();
                userActivityLogMapper
                                .insertActivityLog(userActivityLogVo);
                userActivityLogMapper.insertFollowLog(userActivityLogVo.getId(), targetId, followMethod);
        }

        // BLOCK
        @Async("userLogTaskExecutor")
        public void insertBlockLog(String userId, String ipAddress, String targetId, BlockMethod blockMethod) {
                UserActivityLogVo userActivityLogVo = UserActivityLogVo.builder()
                                .userId(userId)
                                .ipAddress(ipAddress)
                                .activityType(ActivityType.BLOCK)
                                .build();
                userActivityLogMapper
                                .insertActivityLog(userActivityLogVo);
                userActivityLogMapper.insertBlockLog(userActivityLogVo.getId(), targetId, blockMethod);
        }

}
