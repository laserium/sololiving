package com.sololiving.domain.auth.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.restdocs.snippet.Attributes.attributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sololiving.domain.auth.dto.auth.request.SignInRequestDto;
import com.sololiving.domain.auth.dto.auth.response.SignInResponseDto;
import com.sololiving.domain.auth.exception.auth.AuthErrorCode;
import com.sololiving.domain.auth.service.AuthService;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.global.config.AbstractRestDocsConfig;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.security.jwt.dto.response.CreateTokenResponse;
import com.sololiving.global.security.jwt.enums.ClientId;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.restdocs.snippet.Attributes;

import java.time.Duration;

public class AuthControllerTest extends AbstractRestDocsConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("로그인 성공")
    void postSignInSuccess() throws Exception {

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

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "dummyRefreshToken")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "dummyAccessToken")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();
        SignInResponseDto responseDto = SignInResponseDto.builder()
                .expiresIn(Duration.ofMinutes(3))
                .userType(UserType.GENERAL)
                .clientId(ClientId.SOLOLIVING)
                .oauth2UserId("testOauth2UserId")
                .build();

        // when
        Mockito.when(authService.createTokenResponse(Mockito.any(SignInRequestDto.class))).thenReturn(tokenResponse);
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
                                "auth/signin-success", requestFields(
                                        attributes(key("title").value("Request Fields")),
                                        fieldWithPath("userId").description("사용자 아이디"),
                                        fieldWithPath("userPwd").description("사용자 비밀번호"),
                                        fieldWithPath("clientId").description("토큰 발급 기관")
                                                .attributes(Attributes.key("constraint")
                                                        .value("SOLOLIVING(default) / KAKAO / NAVER / GOOGLE"))),
                                responseFields(
                                        attributes(key("title").value("Response Fields")),
                                        fieldWithPath("expiresIn").description("토큰 만료 시간"),
                                        fieldWithPath("userType").description("사용자 타입"),
                                        fieldWithPath("clientId").description("토큰 발급 기관"),
                                        fieldWithPath("oauth2UserId").description("OAuth2 사용자 아이디")))

                )
                .andDo(document("auth/signin",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("로그인 실패 - 사용자 정보 없음")
    void postSignInUserNotFound() throws Exception {

        // given
        SignInRequestDto requestDto = SignInRequestDto.builder()
                .userId("invalidUser")
                .userPwd("wrongPassword")
                .clientId(ClientId.SOLOLIVING)
                .build();

        // when
        Mockito.when(authService.createTokenResponse(Mockito.any(SignInRequestDto.class)))
                .thenThrow(new ErrorException(UserErrorCode.USER_NOT_FOUND));

        // then
        mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(UserErrorCode.USER_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(UserErrorCode.USER_NOT_FOUND.getMessage()))
                .andDo(document("auth/signin-error",
                        responseFields(
                                attributes(key("title").value("Error Response Fields")),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"))));
    }

}
