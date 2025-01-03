package com.sololiving.domain.auth.dto.oauth.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OauthUserExistenceResponseDto {
    private String isOauthUser;
    private String oauth2UserId;
}
