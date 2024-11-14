package com.sololiving.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.user.dto.response.ViewUserSettingResponseDto;

@Mapper
public interface UserSettingMapper {

    // CREATE
    void insertUserSetting(@Param("userId") String userId);

    // DELETE
    void updateToDeletedUser(@Param("userId") String userId, @Param("email") String email);

    // 설정 ON/OFF 통합 메소드
    void updateSetting(@Param("settingType") String settingType, @Param("status") boolean status,
            @Param("userId") String userId);

    ViewUserSettingResponseDto selectUserSetting(@Param("userId") String userId);

    // 푸쉬 알람 공유 여부 조회
    boolean isPushNotificationSharingEnabled(@Param("userId") String userId);

    // 프로필 공유 여부 조회
    boolean isProfileSharingEnabled(@Param("userId") String userId);

    // 작성한 게시글 공유 여부 조회
    boolean isArticleSharingEnabled(@Param("userId") String userId);

    // 작성한 댓글 공유 여부 조회
    boolean isCommentSharingEnabled(@Param("userId") String userId);

    // 작성한 댓글 공유 여부 조회
    boolean isLikedSharingEnabled(@Param("userId") String userId);

}
