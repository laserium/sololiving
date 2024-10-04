package com.sololiving.domain.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.auth.dto.auth.request.SignUpRequestDto;
import com.sololiving.domain.user.dto.request.SignUpVerificationSmsRequestDto.SendSignUpVerificationSmsRequestDto;
import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.mapper.UserAuthMapper;
import com.sololiving.domain.user.vo.UserVo;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.security.jwt.exception.TokenErrorCode;
import com.sololiving.global.security.jwt.service.TokenProvider;
import com.sololiving.global.security.sms.service.SmsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserAuthMapper userAuthMapper;
    private final SmsService smsService;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입 시 휴대폰 인증번호 전송
    public void sendSignUpVerificationSms(SendSignUpVerificationSmsRequestDto requestDto) {
        String contact = requestDto.getContact();
        validateUniqueField(contact, this::isUserContactAvailable, UserErrorCode.CONTACT_ALREADY_EXISTS);
        smsService.sendSms(contact);
    }

    // 회원가입 - 중복데이터 검증
    protected void validateSignUpRequest(SignUpRequestDto requestDto) {
        validateUniqueField(requestDto.getUserId(), this::isUserIdAvailable, UserErrorCode.ID_ALREADY_EXISTS);
        validateUniqueField(requestDto.getEmail(), this::isUserEmailAvailable,
                UserErrorCode.EMAIL_ALREADY_EXISTS);
        validateUniqueField(requestDto.getContact(), this::isUserContactAvailable,
                UserErrorCode.CONTACT_ALREADY_EXISTS);
    }

    // 필드의 유일성 검증
    public void validateUniqueField(String fieldValue, Validator validator, UserErrorCode errorCode) {
        if (!validator.validate(fieldValue)) {
            throw new ErrorException(errorCode);
        }
    }

    // 필드 유효성 검증을 위한 함수형 인터페이스
    @FunctionalInterface
    private interface Validator {
        boolean validate(String value);
    }

    // 중복 검사 - 아이디(존재하면 false 반환)
    public boolean isUserIdAvailable(String userId) {
        return !userAuthMapper.existsByUserId(userId);
    }

    // 중복 검사 - 이메일(존재하면 false 반환)
    public boolean isUserEmailAvailable(String email) {
        return !userAuthMapper.existsByEmail(email);
    }

    // 중복 검사 - 연락처(존재하면 false 반환)
    private boolean isUserContactAvailable(String contact) {
        return !userAuthMapper.existsByContact(contact);
    }

    // 아이디로 유저 찾기
    public UserVo selectByUserId(String userId) {
        UserVo userVo = userAuthMapper.selectByUserId(userId);
        if (userVo != null) {
            return userVo;
        } else
            throw new ErrorException(UserErrorCode.USER_NOT_FOUND);
    }

    // Oauth2UserId로 유저 찾기(회원가입 분기 로직 전용)
    public UserVo selectByOauth2UserId(String userId) {
        UserVo userVo = userAuthMapper.selectByOauth2UserId(userId);
        if (userVo != null) {
            return userVo;
        } else
            return null;
    }

    // 이메일로 유저 찾기
    public UserVo selectByEmail(String email) {
        UserVo userVo = userAuthMapper.selectByEmail(email);
        if (userVo != null) {
            return userVo;
        } else
            throw new ErrorException(UserErrorCode.USER_NOT_FOUND);
    }

    // 아이디로 이메일 찾기
    public String selectEmailByUserId(String userId) {
        String email = userAuthMapper.selectEmailByUserId(userId);
        if (email != null) {
            return email;
        } else
            throw new ErrorException(UserErrorCode.USER_EMAIL_NOT_FOUND);
    }

    // 아이디로 비밀번호 찾기
    public String selectPasswordByUserId(String userId) {
        String password = userAuthMapper.selectPasswordByUserId(userId);
        if (password != null) {
            return password;
        } else
            throw new ErrorException(UserErrorCode.USER_PWD_NOT_FOUND);
    }

    // 이메일로 아이디 찾기
    public String selectUserIdByEmail(String email) {
        String userId = userAuthMapper.selectUserIdByEmail(email);
        if (userId != null) {
            return userId;
        } else
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
    }

    // 아이디와 이메일 검증
    public String validateUserIdAndEmail(String userId, String inputEmail) {
        String email = userAuthMapper.selectEmailByUserId(userId);
        if (email.equals(inputEmail)) {
            return email;
        } else
            throw new ErrorException(UserErrorCode.USER_EMAIL_NOT_FOUND);
    }

    // AT에서 추출한 USER 검증
    public boolean validateUserIdwithAccessToken(String accessToken) {
        String userId = tokenProvider.getUserId(accessToken);
        if (userAuthMapper.existsByUserId(userId)) {
            return true;
        } else
            throw new ErrorException(TokenErrorCode.CANNT_EXTRACT_USER);
    }

    // 사용자 상태 값 NULL 값 예외처리
    public void validateStatus(Status status) {
        if (status == null || (status != Status.ACTIVE && status != Status.BLOCKED && status != Status.WITHDRAWN)) {
            throw new ErrorException(UserErrorCode.NO_USER_STATUS_REQUEST);
        }
    }

    // 임시 비밀번호 설정
    @Transactional
    public void setTempPassword(String userEmail, String tempPassword) {
        UserVo userVo = selectByEmail(userEmail);

        userAuthMapper.updatePassword(bCryptPasswordEncoder.encode(tempPassword), userVo.getUserId());
    }

    // userId 로 userType 확인
    public UserType selectUserTypeByUserId(String userId) {
        return userAuthMapper.selectUserTypeByUserId(userId);
    }
}
