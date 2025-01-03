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
import com.sololiving.domain.log.enums.AuthMethod;
import com.sololiving.domain.log.service.UserActivityLogService;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserAddressRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserBirthRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserContactRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserEmailRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserGenderRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserNicknameRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestICDto.UpdateUserPasswordRequestDto;
import com.sololiving.domain.user.dto.response.ValidateUserContactResponseDto;
import com.sololiving.domain.user.dto.response.ViewUserProfileImageResponseDto;
import com.sololiving.domain.user.enums.Gender;
import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.mapper.UserMapper;
import com.sololiving.domain.user.mapper.UserProfileMapper;
import com.sololiving.domain.user.mapper.UserSettingMapper;
import com.sololiving.domain.user.vo.UserVo;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.security.sms.exception.SmsErrorCode;
import com.sololiving.global.security.sms.service.SmsRedisService;
import com.sololiving.global.security.sms.service.SmsService;
import com.sololiving.global.util.IpAddressUtil;
import com.sololiving.global.util.RandomGenerator;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private static final String USER_NICK_NAME = "익명";
    private static final String USER_DEFAULT_PROFILE_IAMGE_URL = "https://sololiving-media-bucket.s3.ap-northeast-2.amazonaws.com/media/profile-image/default/profile_default.jpg";
    private static final String USER_DEFAULT_PROFILE_IAMGE_NAME = "프로필_이미지";
    private static final long USER_DEFAULT_PROFILE_IAMGE_SIZE = 2867;

    private final UserAuthService userAuthService;
    private final UserMapper userMapper;
    private final UserSettingMapper userSettingMapper;
    private final UserProfileMapper userProfileMapper;
    private final EmailService emailService;
    private final SmsService smsService;
    private final SmsRedisService smsRedisService;
    private final AuthService authService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserActivityLogService userActivityLogService;

    // 회원가입
    public void signUp(SignUpRequestDto requestDto, HttpServletRequest httpServletRequest) {
        userAuthService.validateSignUpRequest(requestDto);

        // 빌더 생성
        UserVo userVo = createUser(requestDto);
        ViewUserProfileImageResponseDto userProfileImageResponseDto = createUserProfileImage(requestDto.getUserId());
        // 저장
        saveUser(userVo, userProfileImageResponseDto);

        // 사용자 행동 로그 처리
        userActivityLogService.insertAuthLog(userVo.getUserId(), IpAddressUtil.getClientIp(httpServletRequest),
                AuthMethod.SIGNUP);
    }

    private UserVo createUser(SignUpRequestDto requestDto) {
        return UserVo.builder()
                .userId(requestDto.getUserId())
                .userPwd(bCryptPasswordEncoder.encode(requestDto.getUserPwd()))
                .oauth2UserId(requestDto.getOauth2UserId())
                .nickname(USER_NICK_NAME)
                .contact(requestDto.getContact())
                .email(requestDto.getEmail())
                .build();
    }

    public ViewUserProfileImageResponseDto createUserProfileImage(String userId) {
        return ViewUserProfileImageResponseDto.builder()
                .userId(userId)
                .imageUrl(USER_DEFAULT_PROFILE_IAMGE_URL)
                .fileName(USER_DEFAULT_PROFILE_IAMGE_NAME)
                .fileSize(USER_DEFAULT_PROFILE_IAMGE_SIZE)
                .build();
    }

    // 회원가입 - 저장
    @Transactional
    private void saveUser(UserVo userVo, ViewUserProfileImageResponseDto userProfileImageResponseDto) {
        userMapper.insertUser(userVo);
        userSettingMapper.insertUserSetting(userVo.getUserId());
        userProfileMapper.insertUserProfileImage(userProfileImageResponseDto);
    }

    // 회원탈퇴
    public void withdraw(String userId, HttpServletRequest httpServletRequest) {
        validateUserId(userId);
        updateToDeletedUser(userId);

        // 사용자 행동 로그 처리
        userActivityLogService.insertAuthLog(userId, IpAddressUtil.getClientIp(httpServletRequest),
                AuthMethod.SIGNUP);
    }

    // 회원탈퇴- 논리적 삭제
    @Transactional
    private void updateToDeletedUser(String userId) {
        String randomId = RandomGenerator.generateRandomId();
        String randomPassword = bCryptPasswordEncoder.encode(RandomGenerator.generateRandomPassword());
        String randomContact = RandomGenerator.generateRandomContact();
        String randomEmail = randomId;

        userMapper.updateToDeletedUser(userId, randomPassword, randomContact, randomEmail);
        userSettingMapper.updateToDeletedUser(userId, randomEmail);
    }

    // 로그인 시 최근 로그인 시간 변경
    public void setLastSignInAt(String userId) {
        userMapper.updateUserLastSignInAt(userId);

    }

    // 회원 상태 변경
    @Transactional
    public void updateStatus(String userId, Status status) {
        userAuthService.validateStatus(status);

        userMapper.updateUserStatus(userId, status);
    }

    // 유저 이메일 변경
    public void sendUpdateNewEmailRequest(String userId, UpdateUserEmailRequestDto requestDto) {
        validateUserId(userId);
        if (requestDto.getEmail() == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
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
    public ValidateUserContactResponseDto checkUpdateUserContact(String userId, String contact) {
        validateUpdateUserContact(userId, contact);
        String randomNum = RandomGenerator.makeRandomNumber();
        smsRedisService.createSmsCertification(contact, randomNum);
        smsService.sendSms(contact, randomNum);
        return ValidateUserContactResponseDto.builder()
                .code(smsRedisService.getSmsCertification(contact))
                .build();

    }

    private void validateUpdateUserContact(String userId,
            String contact) {
        if (contact.length() != 11) {
            throw new ErrorException(UserErrorCode.CONTACT_LENGTH_FAILED);
        }
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
    }

    public void updateUserContact(String userId, UpdateUserContactRequestDto requestDto) {
        validateUserId(userId);
        if (requestDto.getContact() == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        String contact = requestDto.getContact();
        boolean isCorrect = smsService.checkSms(contact, requestDto.getCode());
        if (isCorrect) {
            updateUserContactInDb(userId, contact);
        } else
            throw new ErrorException(SmsErrorCode.CERTIFICATION_NUMBER_INCORRECT);
    }

    @Transactional
    private void updateUserContactInDb(String userId, String contact) {
        userMapper.updateUserContact(userId, contact);
    }

    // 유저 닉네임 변경
    public void updateUserNickname(String userId,
            UpdateUserNicknameRequestDto requestDto) {
        validateUserId(userId);
        if (requestDto.getNickname() == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        String nickname = requestDto.getNickname();
        updateUserNicknameInDb(userId, nickname);
    }

    @Transactional
    private void updateUserNicknameInDb(String userId, String nickname) {
        userMapper.updateUserNickname(userId, nickname);
    }

    // 회원 성별 변경
    public void updateUserGender(String userId, UpdateUserGenderRequestDto requestDto) {
        validateUserId(userId);
        if (requestDto.getGender() == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        Gender gender = requestDto.getGender();
        updateUserGenderInDb(userId, gender);
    }

    @Transactional
    private void updateUserGenderInDb(String userId, Gender gender) {
        userMapper.updateUserGender(userId, gender);
    }

    // 회원 주소 변경
    public void updateUserAddress(String userId, UpdateUserAddressRequestDto requestDto) {
        validateUserId(userId);
        if (requestDto.getAddress() == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        String address = requestDto.getAddress();
        updateUserAddressInDb(userId, address);
    }

    @Transactional
    private void updateUserAddressInDb(String userId, String address) {
        userMapper.updateUserAddress(userId, address);
    }

    // 회원 생일 변경
    public void updateUserBirth(String userId, UpdateUserBirthRequestDto requestDto) {
        validateUserId(userId);
        if (requestDto.getBirth() == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        LocalDate birth = requestDto.getBirth();
        updateUserBirthInDb(userId, birth);
    }

    @Transactional
    private void updateUserBirthInDb(String userId, LocalDate birth) {
        userMapper.updateUserBirth(userId, birth);
    }

    // 회원 비밀번호 변경
    public void updateUserPassword(String userId, UpdateUserPasswordRequestDto requestDto) {
        validateUserId(userId);
        if (requestDto.getNewPassword() == null || requestDto.getPassword() == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        String password = requestDto.getPassword();
        String newPassword = requestDto.getNewPassword();
        String oldPassword = userAuthService.selectPasswordByUserId(userId);
        authService.verifyPassword(oldPassword, password);
        updateUserPasswordInDb(userId, bCryptPasswordEncoder.encode(newPassword));
    }

    @Transactional
    private void updateUserPasswordInDb(String userId, String password) {
        userMapper.updateUserPassword(userId, password);
    }

    private void validateUserId(String userId) {
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
    }
}
