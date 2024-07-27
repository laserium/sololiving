package com.sololiving.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.auth.dto.auth.request.SignInRequestDto;
import com.sololiving.domain.auth.dto.auth.request.SignUpRequestDto;
import com.sololiving.domain.auth.dto.auth.response.SignInResponseDto;
import com.sololiving.domain.auth.dto.email.response.EmailResponseDto;
import com.sololiving.domain.auth.dto.token.response.CreateTokenResponse;
import com.sololiving.domain.auth.exception.AuthErrorCode;
import com.sololiving.domain.auth.exception.AuthSuccessCode;
import com.sololiving.domain.auth.service.AuthEmailService;
import com.sololiving.domain.auth.service.AuthService;
import com.sololiving.domain.user.service.UserService;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final AuthEmailService authEmailService;
    private final CookieService cookieService;

    @PostMapping("/signup")
    public ResponseEntity<?> postSignUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);
        SuccessResponse successResponse = SuccessResponse.builder()
                .code((AuthSuccessCode.SIGN_UP_SUCCESS).getCode())
                .message((AuthSuccessCode.SIGN_UP_SUCCESS).getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

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
                    .body(AuthSuccessCode.SIGN_OUT_SUCCESS);
        } else {
            throw new ErrorException(AuthErrorCode.CANNOT_FIND_RT);
        }
    }

    @PostMapping("/users/id-recover")
    public ResponseEntity<?> postUsersIdRecover(@RequestBody String email) {
        EmailResponseDto emailResponseDto = EmailResponseDto.builder()
                .to(email)
                .subject("[홀로서기] 아이디 찾기 인증 메일입니다.")
                .build();

        authEmailService.sendAuthEmail(email, emailResponseDto, "id-recover");
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/users/password-reset")
    public ResponseEntity<?> postUsersPasswordReset(@RequestBody String userId, @RequestBody String email) {

        EmailResponseDto emailResponseDto = EmailResponseDto.builder()
                .to(userService.validateUserIdAndEmail(userId, email))
                .subject("[홀로서기] 임시 비밀번호 발급 메일입니다.")
                .build();

        authEmailService.sendMail(emailResponseDto, "password");
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
