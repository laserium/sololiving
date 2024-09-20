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
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.domain.user.service.UserService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.security.jwt.dto.response.CreateTokenResponse;
import com.sololiving.global.security.jwt.exception.TokenErrorCode;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserAuthService userAuthService;
    private final UserService userService;
    private final EmailService authEmailService;
    private final CookieService cookieService;

    @PostMapping("/signin")
    public ResponseEntity<?> postSignIn(@RequestBody SignInRequestDto requestDto) {
        CreateTokenResponse tokenResponse = authService.createTokenResponse(requestDto);
        ResponseCookie refreshTokenCookie = authService.createRefreshTokenCookie(tokenResponse.getRefreshToken());
        ResponseCookie accessTokenCookie = authService.createAccessTokenCookie(tokenResponse.getAccessToken());
        SignInResponseDto signInResponse = authService.createSignInResponse(requestDto, tokenResponse);
        userService.setLastSignInAt(requestDto.getUserId());
        return ResponseEntity.status(HttpStatus.OK)
                .header("Set-Cookie", refreshTokenCookie.toString())
                .header("Set-Cookie", accessTokenCookie.toString())
                .body(signInResponse);
    }

    @PostMapping("/signout")
    public ResponseEntity<SuccessResponse> postSignOut(HttpServletRequest httpServletRequest) {
        String refreshTokenValue = cookieService.extractRefreshTokenFromCookie(httpServletRequest);
        if (refreshTokenValue != null) {
            authService.userSignOut(refreshTokenValue);
            ResponseCookie refreshTokencookie = cookieService.deleteRefreshTokenCookie();
            ResponseCookie accessTokenCookie = cookieService.deleteAccessTokenCookie();
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Set-Cookie", refreshTokencookie.toString())
                    .header("Set-Cookie", accessTokenCookie.toString())
                    .body(ResponseMessage.createSuccessResponse(AuthSuccessCode.SIGN_OUT_SUCCESS));
        } else {
            throw new ErrorException(TokenErrorCode.CANNOT_FIND_RT);
        }
    }

    @PostMapping("/users/id-recover")
    public ResponseEntity<SuccessResponse> postUsersIdRecover(@RequestBody IdRecoverRequestDto requestDto) {
        EmailResponseDto emailResponseDto = EmailResponseDto.builder()
                .to(requestDto.getEmail())
                .subject("[홀로서기] 아이디 찾기 인증 메일입니다.")
                .build();

        authEmailService.sendMailIdRecover(requestDto.getEmail(), emailResponseDto, "id-recover");
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(AuthSuccessCode.ID_RECOVER_SUCCESS));
    }

    @PostMapping("/users/password-reset")
    public ResponseEntity<SuccessResponse> postUsersPasswordReset(
            @RequestBody PasswordResetRequestDto requestDto) {

        EmailResponseDto emailResponseDto = EmailResponseDto.builder()
                .to(userAuthService.validateUserIdAndEmail(requestDto.getUserId(),
                        requestDto.getEmail()))
                .subject("[홀로서기] 임시 비밀번호 발급 메일입니다.")
                .build();

        authEmailService.sendMailPasswordReset(emailResponseDto, "password-reset");
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(AuthSuccessCode.PASSWORD_RESET_SUCCESS));
    }

    @PostMapping("/verification")
    public ResponseEntity<SuccessResponse> postVerificationWithData(HttpServletRequest httpServletRequest) {
        String accessToken = cookieService.extractAccessTokenFromCookie(httpServletRequest);
        if (userAuthService.validateUserIdwithAccessToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseMessage.createSuccessResponse(AuthSuccessCode.VERIFY_SUCCESS));
        } else
            throw new ErrorException(AuthErrorCode.VERIFY_FAILED);
    }

}
