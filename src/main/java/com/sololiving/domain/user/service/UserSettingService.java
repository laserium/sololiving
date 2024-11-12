package com.sololiving.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.user.dto.response.ViewUserSettingResponseDto;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.exception.UserSettingErrorCode;
import com.sololiving.domain.user.exception.UserSettingSuccessCode;
import com.sololiving.domain.user.exception.UserSuccessCode;
import com.sololiving.domain.user.mapper.UserSettingMapper;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSettingService {

    private final UserSettingMapper userSettingMapper;

    // 설정 ON/OFF 통합 메소드
    @Transactional
    public UserSettingSuccessCode toggleSetting(String settingType, boolean status, String userId) {
        userSettingMapper.updateSetting(settingType, status, userId);

        // 설정 유형별 성공 코드를 반환
        switch (settingType.toLowerCase()) {
            case "push-notification":
                return status ? UserSettingSuccessCode.PUSH_NOTIFICATION_ON
                        : UserSettingSuccessCode.PUSH_NOTIFICATION_OFF;
            case "profile-sharing":
                return status ? UserSettingSuccessCode.PROFILE_SHARING_ON : UserSettingSuccessCode.PROFILE_SHARING_OFF;
            case "article-sharing":
                return status ? UserSettingSuccessCode.ARTICLE_SHARING_ON : UserSettingSuccessCode.ARTICLE_SHARING_OFF;
            case "comment-sharing":
                return status ? UserSettingSuccessCode.COMMENT_SHARING_ON : UserSettingSuccessCode.COMMENT_SHARING_OFF;
            case "liked-sharing":
                return status ? UserSettingSuccessCode.LIKED_SHARING_ON : UserSettingSuccessCode.LIKED_SHARING_OFF;
            default:
                throw new ErrorException(UserSettingErrorCode.INVALID_SETTING_TYPE);
        }
    }

    public ViewUserSettingResponseDto viewUserSetting(String userId) {
        return userSettingMapper.selectUserSetting(userId);
    }
}
