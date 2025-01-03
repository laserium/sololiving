package com.sololiving.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.auth.dto.auth.request.IdRecoverRequestDto;
import com.sololiving.domain.auth.dto.auth.request.PasswordResetRequestDto;
import com.sololiving.domain.auth.dto.auth.request.SignInRequestDto;
import com.sololiving.domain.auth.dto.auth.response.SignInResponseDto;
import com.sololiving.domain.auth.exception.auth.AuthErrorCode;
import com.sololiving.domain.auth.exception.auth.AuthSuccessCode;
import com.sololiving.domain.auth.service.AuthService;
import com.sololiving.domain.email.dto.response.EmailResponseDto;
import com.sololiving.domain.email.service.EmailService;
import com.sololiving.domain.log.enums.AuthMethod;
import com.sololiving.domain.log.service.UserActivityLogService;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.domain.user.service.UserService;
import com.sololiving.global.exception.GlobalErrorCode;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.security.jwt.dto.response.CreateTokenResponse;
import com.sololiving.global.security.jwt.exception.TokenErrorCode;
import com.sololiving.global.security.jwt.service.TokenProvider;
import com.sololiving.global.util.CookieService;
import com.sololiving.global.util.IpAddressUtil;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserAuthService userAuthService;
    private final UserService userService;
    private final EmailService authEmailService;
    private final CookieService cookieService;
    private final TokenProvider tokenProvider;
    private final UserActivityLogService userActivityLogService;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        CreateTokenResponse tokenResponse = authService.createTokenResponse(requestDto);
        ResponseCookie refreshTokenCookie = authService.createRefreshTokenCookie(tokenResponse.getRefreshToken());
        ResponseCookie accessTokenCookie = authService.createAccessTokenCookie(tokenResponse.getAccessToken());
        SignInResponseDto signInResponse = authService.createSignInResponse(requestDto, tokenResponse);
        userService.setLastSignInAt(requestDto.getUserId());

        // 사용자 행동 로그 처리
        userActivityLogService.insertAuthLog(requestDto.getUserId(), IpAddressUtil.getClientIp(httpServletRequest),
                AuthMethod.SIGNIN);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Set-Cookie", refreshTokenCookie.toString())
                .header("Set-Cookie", accessTokenCookie.toString())
                .body(signInResponse);
    }

    @PostMapping("/signout")
    public ResponseEntity<SuccessResponse> signOut(HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        String refreshTokenValue = cookieService.extractRefreshTokenFromCookie(httpServletRequest);
        if (refreshTokenValue != null) {
            authService.userSignOut(refreshTokenValue);
            ResponseCookie refreshTokencookie = cookieService.deleteRefreshTokenCookie();
            ResponseCookie accessTokenCookie = cookieService.deleteAccessTokenCookie();
            // 사용자 행동 로그 처리
            userActivityLogService.insertAuthLog(userId, IpAddressUtil.getClientIp(httpServletRequest),
                    AuthMethod.SIGNOUT);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Set-Cookie", refreshTokencookie.toString())
                    .header("Set-Cookie", accessTokenCookie.toString())
                    .body(ResponseMessage.createSuccessResponse(AuthSuccessCode.SIGN_OUT_SUCCESS));
        } else {
            throw new ErrorException(TokenErrorCode.CANNOT_FIND_RT);
        }
    }

    @PostMapping("/users/id-recover")
    public ResponseEntity<SuccessResponse> recoverUserId(@RequestBody IdRecoverRequestDto requestDto) {
        if (requestDto.getEmail() == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        String email = requestDto.getEmail();
        if (userAuthService.isUserEmailAvailable(email)) {
            throw new ErrorException(AuthErrorCode.NO_EMAIL_IN_DB);
        }
        EmailResponseDto emailResponseDto = EmailResponseDto.builder()
                .to(email)
                .subject("[홀로서기] 아이디 찾기 인증 메일입니다.")
                .build();

        authEmailService.sendMailIdRecover(email, emailResponseDto, "id-recover");
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(AuthSuccessCode.ID_RECOVER_SUCCESS));
    }

    @PostMapping("/users/password-reset")
    public ResponseEntity<SuccessResponse> resetUserPassword(
            @RequestBody PasswordResetRequestDto requestDto) {
        if (requestDto.getEmail() == null || requestDto.getUserId() == null) {
            throw new ErrorException(GlobalErrorCode.REQUEST_IS_NULL);
        }
        EmailResponseDto emailResponseDto = EmailResponseDto.builder()
                .to(userAuthService.validateUserIdAndEmail(requestDto.getUserId(),
                        requestDto.getEmail()))
                .subject("[홀로서기] 임시 비밀번호 발급 메일입니다.")
                .build();

        authEmailService.sendMailPasswordReset(emailResponseDto, "password-reset");
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(AuthSuccessCode.PASSWORD_RESET_SUCCESS));
    }

    // useEffect 인증 => 인증 성공 시 토큰 재발급, 인증 실패 시 자동 로그아웃
    @PostMapping("/verification")
    public ResponseEntity<SuccessResponse> postVerificationWithData(HttpServletRequest httpServletRequest) {
        String accessToken = cookieService.extractAccessTokenFromCookie(httpServletRequest);
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        if (!tokenProvider.validToken(accessToken)) {
            throw new ErrorException(AuthErrorCode.VERIFY_FAILED);
        }
        String newAccessToken = tokenProvider.generateTokenVer2(userId, userAuthService.selectEmailByUserId(userId),
                TokenProvider.ACCESS_TOKEN_DURATION);
        ResponseCookie accessTokenCookie = authService.createAccessTokenCookie(newAccessToken);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Set-Cookie", accessTokenCookie.toString())
                .body(ResponseMessage.createSuccessResponse(AuthSuccessCode.VERIFY_SUCCESS));
    }

    @PostMapping("/admin-verification")
    public ResponseEntity<SuccessResponse> adminVerification(HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        userAuthService.isAdmin(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(AuthSuccessCode.ADMIN_VERIFY_SUCCESS));
    }

}
