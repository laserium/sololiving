package com.sololiving.domain.auth.exception;

import com.sololiving.global.exception.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    ID_ALREADY_EXISTS("AUTH_E001", "아이디가 이미 존재합니다."),
    EMAIL_ALREADY_EXISTS("AUTH_E002", "이메일이 이미 존재합니다."),
    CONTACT_ALREADY_EXISTS("AUTH_E003", "연락처가 이미 존재합니다."),
    PASSWORD_INCORRECT("AUTH_E004", "비밀번호가 일치하지 않습니다."),
    CANNOT_FIND_RT("AUTH_E005", "해당하는 유저의 RefreshToken을 찾을 수 없습니다."),
    FAIL_TO_RETRIVE_KAKAO_TOKEN("AUTH_E006", "카카오 로그인 관련 토큰을 회수할 수 없습니다."),
    MISSING_OAUTH2_CONFIGURATION_PROPERTIES("AUTH_E007", "OAUTH2 관련 정보를 찾을 수 없습니다."),
    FAIL_TO_RETRIEVE_USER_INFO("AUTH_E008", "사용자 정보 조회 실패(OAuth2.0, NAVER)"),
    WRONG_PARAMETER_OR_REQUEST("AUTH_E009", "파라미터가 잘못되었거나 요청문이 잘못되었습니다."),
    CANNOT_REFRESH_TOKEN("AUTH_E010", "토큰 갱신 중 오류발생"),
    CANNOT_DELETE_REFRESH_TOKEN("AUTH_E011", "RefreshToken을 삭제할 수 없습니다."),
    CANNOT_SIGN_OUT("AUTH_E012", "로그아웃을 할 수 없습니다.");
    

    private final String code;
    private final String message;

}
