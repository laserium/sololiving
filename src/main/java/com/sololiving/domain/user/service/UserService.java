package com.sololiving.domain.user.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.auth.dto.auth.request.SignUpRequestDto;
import com.sololiving.domain.email.dto.response.EmailResponseDto;
import com.sololiving.domain.email.service.EmailService;
import com.sololiving.domain.email.vo.EmailVerificationTokenVo;
import com.sololiving.domain.user.dto.request.UpdateUserEmailRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserGenderRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserNicknameRequestDto;
import com.sololiving.domain.user.enums.Gender;
import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.mapper.UserMapper;
import com.sololiving.domain.user.vo.UserVo;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.security.jwt.service.TokenProvider;
import com.sololiving.global.security.jwt.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private static final String USER_NICK_NAME = "익명";
    private final UserAuthService userAuthService;
    private final TokenService tokenService;
    private final TokenProvider tokenProvider;
    private final UserMapper userMapper;
    private final EmailService authEmailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    public void signUp(SignUpRequestDto signUpRequestDto) {
        userAuthService.validateSignUpRequest(signUpRequestDto);
        saveUser(signUpRequestDto);
    }

    // 회원가입 - 저장
    @Transactional
    private void saveUser(SignUpRequestDto signUpRequestDto) {
        UserVo user = UserVo.builder()
                .userId(signUpRequestDto.getUserId())
                .userPwd(bCryptPasswordEncoder.encode(signUpRequestDto.getUserPwd()))
                .oauth2UserId(signUpRequestDto.getOauth2UserId())
                .nickname(USER_NICK_NAME)
                .contact(signUpRequestDto.getContact())
                .email(signUpRequestDto.getEmail())
                .gender(Gender.DEFAULT)
                .address(null)
                .birth(null)
                .status(Status.ACTIVE)
                .followersCnt("0")
                .followingCnt("0")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userType(UserType.GENERAL)
                .lastSignInAt(null)
                .lastActivityAt(LocalDateTime.now())
                .build();

        userMapper.insertUser(user);
    }

    // 회원탈퇴
    public void deleteUserRequest(String accessToken) {
        if (userAuthService.validateUserIdwithAccessToken(accessToken)) {
            String userId = tokenProvider.getUserId(accessToken);
            deleteUser(userId);
        } else
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
    }

    // 회원탈퇴 - 삭제
    @Transactional
    private void deleteUser(String userId) {
        userMapper.deleteByUserId(userId);
    }

    // 로그인 시 최근 로그인 시간 변경
    public void setLastSignInAt(String userId) {
        userMapper.updateUserLastSignInAt(userId);
    }

    // 회원 상태 변경
    @Transactional
    public void updateStatus(String accessToken, Status status) {
        tokenService.validateAccessToken(accessToken);
        String userId = tokenProvider.getUserId(accessToken);
        validateUserId(userId);
        userAuthService.validateStatus(status);
        if (userAuthService.findUserTypeByUserId(userId) == UserType.ADMIN) {
            userMapper.updateUserStatus(userId, status);
        } else
            throw new ErrorException(UserErrorCode.USER_TYPE_ERROR_NO_PERMISSION);
    }

    // 유저 이메일 변경
    public void sendUpdateNewEmailRequest(String accessToken, UpdateUserEmailRequestDto patchUsersEmailRequestDto) {
        String userId = tokenProvider.getUserId(accessToken);
        validateUserId(userId);
        String email = patchUsersEmailRequestDto.getEmail();
        if (userAuthService.isUserEmailAvailable(email)) {
            EmailResponseDto emailResponseDto = EmailResponseDto.builder()
                    .to(email)
                    .subject("[홀로서기] 새로운 이메일 설정 확인 메일입니다.")
                    .build();
            authEmailService.sendMailUpdateEmail(email, userId, emailResponseDto, "update-email");
        } else {
            throw new ErrorException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    // 이메일 수정 후 저장
    public void updateUserEmail(EmailVerificationTokenVo emailVerificationTokenVo) {
        String userId = emailVerificationTokenVo.getUserId();
        String email = emailVerificationTokenVo.getNewEmail();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        } else {
            updateUserEmailInDb(userId, email);
        }
    }

    // 회원 이메일 - 수정
    @Transactional
    private void updateUserEmailInDb(String userId, String email) {
        userMapper.updateUserEmail(userId, email);
    }

    // 유저 닉네임 변경
    public void updateUserNickname(String accessToken,
            UpdateUserNicknameRequestDto updateUsersNicknameRequestDto) {
        String userId = tokenProvider.getUserId(accessToken);
        String nickname = updateUsersNicknameRequestDto.getNickname();
        validateUserId(userId);
        if (nickname == null) {
            throw new ErrorException(UserErrorCode.USER_NICKNAME_IS_NULL);
        }
        updateUserNicknameInDb(userId, nickname);
    }

    @Transactional
    private void updateUserNicknameInDb(String userId, String nickname) {
        userMapper.updateUserNickname(userId, nickname);
    }

    private void validateUserId(String userId) {
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
    }

    // 회원 성별 변경
    public void updateUserGender(String accessToken, UpdateUserGenderRequestDto updateUserGenderRequestDto) {
        String userId = tokenProvider.getUserId(accessToken);
        Gender gender = updateUserGenderRequestDto.getGender();
        validateUserId(userId);
        if (gender == null) {
            throw new ErrorException(UserErrorCode.USER_GENDER_IS_NULL);
        }
        updateUserGenderInDb(userId, gender);
    }

    @Transactional
    private void updateUserGenderInDb(String userId, Gender gender) {
        userMapper.updateUserGender(userId, gender);
    }

}
