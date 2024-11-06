package com.sololiving.domain.user.service;

import java.time.LocalDate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.auth.dto.auth.request.SignUpRequestDto;
import com.sololiving.domain.auth.service.AuthService;
import com.sololiving.domain.email.dto.response.EmailResponseDto;
import com.sololiving.domain.email.service.EmailService;
import com.sololiving.domain.email.vo.EmailVerificationTokenVo;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserAddressRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserBirthRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserContactRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserEmailRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserGenderRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserNicknameRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserPasswordRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.ValidateUpdateUserContactRequestDto;
import com.sololiving.domain.user.enums.Gender;
import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.mapper.UserMapper;
import com.sololiving.domain.user.vo.UserVo;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.security.jwt.service.TokenProvider;
import com.sololiving.global.security.jwt.service.AccessTokenService;
import com.sololiving.global.security.sms.exception.SmsErrorCode;
import com.sololiving.global.security.sms.service.SmsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private static final String USER_NICK_NAME = "익명";
    private final UserAuthService userAuthService;
    private final AccessTokenService accessTokenService;
    private final TokenProvider tokenProvider;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final SmsService smsService;
    private final AuthService authService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    public void signUp(SignUpRequestDto requestDto) {
        userAuthService.validateSignUpRequest(requestDto);
        saveUser(requestDto);
    }

    // 회원가입 - 저장
    @Transactional
    private void saveUser(SignUpRequestDto requestDto) {
        UserVo user = UserVo.builder()
                .userId(requestDto.getUserId())
                .userPwd(bCryptPasswordEncoder.encode(requestDto.getUserPwd()))
                .oauth2UserId(requestDto.getOauth2UserId())
                .nickname(USER_NICK_NAME)
                .contact(requestDto.getContact())
                .email(requestDto.getEmail())
                .build();

        userMapper.insertUser(user);
    }

    // 회원탈퇴
    public void deleteUserRequest(String accessToken) {
        String userId = tokenProvider.getUserId(accessToken);
        if (userId == null && userAuthService.isUserIdAvailable(userId) == true) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        // 카테고리 관리자일 경우 관리자를 admin 으로 변경
        deleteUser(userId);
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
        accessTokenService.checkAccessToken(accessToken);
        String userId = tokenProvider.getUserId(accessToken);
        validateUserId(userId);
        userAuthService.validateStatus(status);
        if (userAuthService.selectUserTypeByUserId(userId) == UserType.ADMIN) {
            userMapper.updateUserStatus(userId, status);
        } else
            throw new ErrorException(UserErrorCode.USER_TYPE_ERROR_NO_PERMISSION);
    }

    // 유저 이메일 변경
    public void sendUpdateNewEmailRequest(String accessToken, UpdateUserEmailRequestDto requestDto) {
        String userId = tokenProvider.getUserId(accessToken);
        validateUserId(userId);
        String email = requestDto.getEmail();
        if (userAuthService.isUserEmailAvailable(email)) {
            EmailResponseDto emailResponseDto = EmailResponseDto.builder()
                    .to(email)
                    .subject("[홀로서기] 새로운 이메일 설정 확인 메일입니다.")
                    .build();
            emailService.sendMailUpdateEmail(email, userId, emailResponseDto, "update-email");
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

    // 회원 연락처 변경 전 인증 메일 전송
    public String validateUpdateUserContact(String accessToken,
            ValidateUpdateUserContactRequestDto requestDto) {
        String userId = tokenProvider.getUserId(accessToken);
        String contact = requestDto.getContact();
        validateUserId(userId);
        if (contact == null) {
            throw new ErrorException(UserErrorCode.UPDATE_USER_REQUEST_DATA_IS_NULL);
        }
        return contact;
    }

    public void updateUserContact(String accessToken, UpdateUserContactRequestDto requestDto) {
        String userId = tokenProvider.getUserId(accessToken);
        validateUserId(userId);
        String contact = requestDto.getContact();
        boolean isCorrect = smsService.checkSms(contact, requestDto.getCode());
        if (isCorrect) {
            updateUserContactInDb(userId, contact);
        } else
            throw new ErrorException(SmsErrorCode.CERTIFICATION_NUMBER_INCORRECT);
    }

    private void updateUserContactInDb(String userId, String contact) {
        userMapper.updateUserContact(userId, contact);
    }

    // 유저 닉네임 변경
    public void updateUserNickname(String accessToken,
            UpdateUserNicknameRequestDto requestDto) {
        String userId = tokenProvider.getUserId(accessToken);
        String nickname = requestDto.getNickname();
        validateUserId(userId);
        if (nickname == null) {
            throw new ErrorException(UserErrorCode.UPDATE_USER_REQUEST_DATA_IS_NULL);
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
    public void updateUserGender(String accessToken, UpdateUserGenderRequestDto requestDto) {
        String userId = tokenProvider.getUserId(accessToken);
        Gender gender = requestDto.getGender();
        validateUserId(userId);
        if (gender == null) {
            throw new ErrorException(UserErrorCode.UPDATE_USER_REQUEST_DATA_IS_NULL);
        }
        updateUserGenderInDb(userId, gender);
    }

    @Transactional
    private void updateUserGenderInDb(String userId, Gender gender) {
        userMapper.updateUserGender(userId, gender);
    }

    // 회원 주소 변경
    public void updateUserAddress(String accessToken, UpdateUserAddressRequestDto requestDto) {
        String userId = tokenProvider.getUserId(accessToken);
        String address = requestDto.getAddress();
        validateUserId(userId);
        if (address == null) {
            throw new ErrorException(UserErrorCode.UPDATE_USER_REQUEST_DATA_IS_NULL);
        }
        updateUserAddressInDb(userId, address);
    }

    @Transactional
    private void updateUserAddressInDb(String userId, String address) {
        userMapper.updateUserAddress(userId, address);
    }

    // 회원 생일 변경
    public void updateUserBirth(String accessToken, UpdateUserBirthRequestDto requestDto) {
        String userId = tokenProvider.getUserId(accessToken);
        LocalDate birth = requestDto.getBirth();
        validateUserId(userId);
        if (birth == null) {
            throw new ErrorException(UserErrorCode.UPDATE_USER_REQUEST_DATA_IS_NULL);
        }
        updateUserBirthInDb(userId, birth);
    }

    @Transactional
    private void updateUserBirthInDb(String userId, LocalDate birth) {
        userMapper.updateUserBirth(userId, birth);
    }

    // 회원 비밀번호 변경
    public void updateUserPassword(String accessToken, UpdateUserPasswordRequestDto requestDto) {
        String userId = tokenProvider.getUserId(accessToken);
        validateUserId(userId);
        String oldPassword = userAuthService.selectPasswordByUserId(userId);
        String password = requestDto.getPassword();
        authService.verifyPassword(oldPassword, password);
        String newPassword = requestDto.getNewPassword();
        if (newPassword == null) {
            new ErrorException(UserErrorCode.UPDATE_USER_REQUEST_DATA_IS_NULL);
        }
        updateUserPasswordInDb(userId, bCryptPasswordEncoder.encode(newPassword));
    }

    @Transactional
    private void updateUserPasswordInDb(String userId, String password) {
        userMapper.updateUserPassword(userId, password);
    }

}
