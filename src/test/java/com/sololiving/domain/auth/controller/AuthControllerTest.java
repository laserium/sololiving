package com.sololiving.domain.auth.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;

import com.sololiving.domain.auth.dto.auth.request.IdRecoverRequestDto;
import com.sololiving.domain.auth.dto.auth.request.PasswordResetRequestDto;
import com.sololiving.domain.auth.dto.auth.request.SignInRequestDto;
import com.sololiving.domain.auth.dto.auth.response.SignInResponseDto;
import com.sololiving.domain.auth.exception.auth.AuthErrorCode;
import com.sololiving.domain.auth.service.AuthService;
import com.sololiving.domain.email.dto.response.EmailResponseDto;
import com.sololiving.domain.email.service.EmailService;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.global.config.AbstractRestDocsConfig;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.security.jwt.dto.response.CreateTokenResponse;
import com.sololiving.global.security.jwt.enums.ClientId;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.Duration;

public class AuthControllerTest extends AbstractRestDocsConfig {

    @MockBean
    private AuthService authService;

    @MockBean
    private CookieService cookieService;

    @MockBean
    private EmailService emailService;

    @Test
    @DisplayName("로그인 - 테스트")
    void postSignInTest() throws Exception {

        // given
        SignInRequestDto requestDto = SignInRequestDto.builder()
                .userId("user001")
                .userPwd("1234")
                .clientId(ClientId.SOLOLIVING)
                .build();

        CreateTokenResponse tokenResponse = CreateTokenResponse.builder()
                .refreshToken("dummyRefreshToken")
                .accessToken("dummyAccessToken")
                .expiresIn(Duration.ofMinutes(3))
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "testRefreshToken")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build();
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "testAccessToken")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(30))
                .build();
        SignInResponseDto responseDto = SignInResponseDto.builder()
                .expiresIn(Duration.ofMinutes(30))
                .userType(UserType.GENERAL)
                .clientId(ClientId.SOLOLIVING)
                .oauth2UserId("testOauth2UserId")
                .build();

        // when
        Mockito.when(authService.createTokenResponse(Mockito.any(SignInRequestDto.class))).thenReturn(tokenResponse)
                .thenThrow(new ErrorException(UserErrorCode.USER_NOT_FOUND))
                .thenThrow(new ErrorException(AuthErrorCode.PASSWORD_INCORRECT));
        Mockito.when(authService.createRefreshTokenCookie(Mockito.anyString())).thenReturn(refreshTokenCookie);
        Mockito.when(authService.createAccessTokenCookie(Mockito.anyString())).thenReturn(accessTokenCookie);
        Mockito.when(authService.createSignInResponse(Mockito.any(SignInRequestDto.class),
                Mockito.any(CreateTokenResponse.class))).thenReturn(responseDto);

        // then
        mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "auth/signin",
                                requestFields(
                                        fieldWithPath("userId").type(JsonFieldType.STRING).description("사용자 아이디"),
                                        fieldWithPath("userPwd").type(JsonFieldType.STRING).description("사용자 비밀번호"),
                                        fieldWithPath("clientId").description("토큰 발급 기관")
                                                .attributes(key("constraint")
                                                        .value("SOLOLIVING(default) / KAKAO / NAVER / GOOGLE"))),
                                responseFields(
                                        fieldWithPath("expiresIn").type(JsonFieldType.NUMBER).description("토큰 만료 시간"),
                                        fieldWithPath("userType").type(JsonFieldType.STRING).description("사용자 타입"),
                                        fieldWithPath("clientId").type(JsonFieldType.STRING).description("토큰 발급 기관"),
                                        fieldWithPath("oauth2UserId").type(JsonFieldType.STRING)
                                                .description("OAuth2 사용자 아이디")))

                )
                .andDo(document("auth/signin",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("로그아웃 - 테스트")
    void postSignOutTest() throws Exception {
        // given
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "testRefreshToken")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build();
        // ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken",
        // "testAccessToken")
        // .httpOnly(true)
        // .secure(true)
        // .path("/")
        // .maxAge(Duration.ofMinutes(30))
        // .build();

        // when
        Mockito.when(cookieService.extractRefreshTokenFromCookie(Mockito.any(HttpServletRequest.class)))
                .thenReturn(refreshTokenCookie.getValue());
        Mockito.when(cookieService.deleteRefreshTokenCookie())
                .thenReturn(ResponseCookie.from("refreshToken", "").build());
        Mockito.when(cookieService.deleteAccessTokenCookie())
                .thenReturn(ResponseCookie.from("accessToken", "").build());

        // then
        mockMvc.perform(post("/auth/signout")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Set-Cookie", "refreshToken=testRefreshToken")
                .header("Set-Cookie", "accessToken=testAccessToken"))
                .andExpect(status().isOk())
                .andExpect(header().stringValues("Set-Cookie",
                        "refreshToken=", "accessToken="))
                .andDo(document("/auth/signout",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Set-Cookie").description("accessToken = 엑세스토큰")),
                        requestHeaders(headerWithName("Set-Cookie").description("refreshToken = 리프레쉬토큰"))));
    }

    @Test
    @DisplayName("아이디 찾기 - 테스트")
    void postUsersIdRecoverTest() throws Exception {
        // given
        IdRecoverRequestDto requestDto = IdRecoverRequestDto.builder()
                .email("testUser@naver.com")
                .build();
        EmailResponseDto emailResponseDto = EmailResponseDto.builder()
                .to(requestDto.getEmail())
                .subject("[홀로서기] 아이디 찾기 인증 메일입니다.")
                .build();

        Mockito.doNothing().when(emailService).sendMailIdRecover(
                Mockito.eq(requestDto.getEmail()),
                Mockito.eq(emailResponseDto),
                Mockito.eq("id-recover"));

        mockMvc.perform(post("/auth/users/id-recover")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(document("auth/users/id-recover",
                        requestFields(fieldWithPath("email").description("사용자의 이메일 주소"))))
                .andDo(document("auth/users/id-recover",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("비밀번호 찾기 - 테스트")
    void postUsersPasswordResetTest() throws Exception {
        // given
        PasswordResetRequestDto requestDto = PasswordResetRequestDto.builder()
                .userId("testUser")
                .email("testUser@naver.com")
                .build();
        EmailResponseDto emailResponseDto = EmailResponseDto.builder()
                .to(requestDto.getEmail())
                .subject("[홀로서기] 임시 비밀번호 발급 메일입니다.")
                .build();

        Mockito.doNothing().when(emailService).sendMailPasswordReset(
                Mockito.eq(emailResponseDto),
                Mockito.eq("password-reset"));

        mockMvc.perform(post("/auth/users/password-reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(document("auth/users/password-reset",
                        requestFields(
                                fieldWithPath("userId").description("사용자의 아이디"),
                                fieldWithPath("email").description("사용자의 이메일 주소"))))
                .andDo(document("auth/users/password-reset",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}
