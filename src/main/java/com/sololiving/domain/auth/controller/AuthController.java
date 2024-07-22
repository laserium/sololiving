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
import com.sololiving.domain.auth.dto.token.response.CreateTokenResponse;
import com.sololiving.domain.auth.exception.AuthErrorCode;
import com.sololiving.domain.auth.exception.AuthSuccessCode;
import com.sololiving.domain.auth.service.AuthService;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.error.ErrorResponse;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService authService;
    private final CookieService cookieService;
    
    @PostMapping("/signup")
    public ResponseEntity<?> postSignUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);
        ErrorResponse errorResponse = ErrorResponse.builder()
                                    .code((AuthSuccessCode.SIGN_UP_SUCCESS).getCode())
                                    .message((AuthSuccessCode.SIGN_UP_SUCCESS).getMessage())
                                    .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(errorResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDto> postSignIn(@RequestBody SignInRequestDto signInRequestDto) {
        CreateTokenResponse tokenResponse = authService.signIn(signInRequestDto);
        ResponseCookie refreshTokenCookie = authService.createRefreshTokenCookie(tokenResponse.getRefreshToken());
        ResponseCookie accessTokenCookie = authService.createAccessTokenCookie(tokenResponse.getAccessToken());
        SignInResponseDto signInResponse;
        try {
            signInResponse = authService.createSignInResponse(signInRequestDto, tokenResponse);
        } catch (ErrorException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        
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
            ResponseCookie cookie = cookieService.deleteRefreshTokenCookie();
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Set-Cookie", cookie.toString())
                    .body(AuthSuccessCode.SIGN_OUT_SUCCESS);
        } else {
            throw new ErrorException(AuthErrorCode.CANNOT_FIND_RT);
        }
    }

}
