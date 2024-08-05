package com.sololiving.domain.user.exception;

import com.sololiving.global.exception.success.SuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserSuccessCode implements SuccessCode {

    SIGN_UP_SUCCESS("USER_S001", "회원가입에 성공하셨습니다."),
    USER_DELETE_SUCCESS("USER_S002", "회원탈퇴에 성공하셨습니다."),
    USER_STATUS_ACTIVE("USER_S003", "회원상태가 성공적으로 '활동'으로 변경되었습니다."),
    USER_STATUS_BLOCKED("USER_S004", "회원상태가 성공적으로 '차단'으로 변경되었습니다."),
    USER_STATUS_WITHDRAWN("USER_S005", "회원상태가 성공적으로 '탈퇴'로 변경되었습니다."),
    UPDATE_EMAIL_REQUEST_SUCCESS("USER_S006", "이메일 변경 요청 메일 전송 완료");

    private final String code;
    private final String message;

}
