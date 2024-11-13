package com.sololiving.domain.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ViewUserSettingResponseDto {

    private boolean pushNotificationEnabled;
    private boolean profileSharingEnabled;
    private boolean articleSharingEnabled;
    private boolean commentSharingEnabled;
    private boolean likedSharingEnabled;

}
