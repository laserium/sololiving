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
import com.sololiving.domain.auth.dto.email.response.EmailResponseDto;
import com.sololiving.domain.auth.dto.token.response.CreateTokenResponse;
import com.sololiving.domain.auth.exception.auth.AuthSuccessCode;
import com.sololiving.domain.auth.exception.token.TokenErrorCode;
import com.sololiving.domain.auth.service.AuthEmailService;
import com.sololiving.domain.auth.service.AuthService;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserAuthService userAuthService;
    private final AuthEmailService authEmailService;
    private final CookieService cookieService;


    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDto> postSignIn(@RequestBody SignInRequestDto signInRequestDto) {
        CreateTokenResponse tokenResponse = authService.createTokenResponse(signInRequestDto);
        ResponseCookie refreshTokenCookie = authService.createRefreshTokenCookie(tokenResponse.getRefreshToken());
        ResponseCookie accessTokenCookie = authService.createAccessTokenCookie(tokenResponse.getAccessToken());
        SignInResponseDto signInResponse = authService.createSignInResponse(signInRequestDto, tokenResponse);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Set-Cookie", refreshTokenCookie.toString())
                .header("Set-Cookie", accessTokenCookie.toString())
                .body(signInResponse);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> postSignOut(HttpServletRequest httpServletRequest) {
        String refreshTokenValue = CookieService.extractRefreshTokenFromCookie(httpServletRequest);
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
    public ResponseEntity<?> postUsersIdRecover(@RequestBody IdRecoverRequestDto idRecoverRequestDto) {
        EmailResponseDto emailResponseDto = EmailResponseDto.builder()
                .to(idRecoverRequestDto.getEmail())
                .subject("[홀로서기] 아이디 찾기 인증 메일입니다.")
                .build();

        authEmailService.sendMailIdRecover(idRecoverRequestDto.getEmail(), emailResponseDto, "id-recover");
        return ResponseEntity.status(HttpStatus.OK)
        .body(ResponseMessage.createSuccessResponse(AuthSuccessCode.ID_RECOVER_SUCCESS));
    }

    @PostMapping("/users/password-reset")
    public ResponseEntity<?> postUsersPasswordReset(@RequestBody PasswordResetRequestDto passwordResetRequestDto) {

        EmailResponseDto emailResponseDto = EmailResponseDto.builder()
                .to(userAuthService.validateUserIdAndEmail(passwordResetRequestDto.getUserId(), passwordResetRequestDto.getEmail()))
                .subject("[홀로서기] 임시 비밀번호 발급 메일입니다.")
                .build();

        authEmailService.sendMailPasswordReset(emailResponseDto, "password-reset");
        return ResponseEntity.status(HttpStatus.OK)
        .body(ResponseMessage.createSuccessResponse(AuthSuccessCode.PASSWORD_RESET_SUCCESS));
    }

}
