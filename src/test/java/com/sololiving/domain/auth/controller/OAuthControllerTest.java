// package com.sololiving.domain.auth.controller;

// import static
// org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
// import static
// org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
// import static
// org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
// import static
// org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
// import static
// org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
// import static
// org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
// import static
// org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseCookie;

// import java.time.Duration;

// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.boot.test.mock.mockito.MockBean;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.sololiving.domain.auth.dto.auth.response.SignInResponseDto;
// import
// com.sololiving.domain.auth.dto.oauth.request.CreateOAuthTokenRequestDto;
// import
// com.sololiving.domain.auth.dto.oauth.response.OauthUserExistenceResponseDto;
// import com.sololiving.domain.auth.service.AuthService;
// import com.sololiving.domain.auth.service.NaverOAuthService;
// import com.sololiving.domain.auth.service.OAuthService;
// import com.sololiving.domain.user.enums.UserType;
// import com.sololiving.domain.user.vo.UserVo;
// import com.sololiving.global.config.AbstractRestDocsConfig;
// import com.sololiving.global.security.jwt.dto.response.AuthTokenResponseDto;
// import com.sololiving.global.security.jwt.enums.ClientId;
// import com.sololiving.global.util.TestDataFactory;

// public class OAuthControllerTest extends AbstractRestDocsConfig {

// @MockBean
// private NaverOAuthService naverOAuthService;

// @MockBean
// private OAuthService oAuthService;

// @MockBean
// private AuthService authService;

// @Test
// @DisplayName("네이버 OAuth 토큰으로 로그인 성공 테스트")
// void postNaverTokenSignInTest() throws Exception {

// // given
// CreateOAuthTokenRequestDto requestDto = CreateOAuthTokenRequestDto.builder()
// .authCode("testAuthCode")
// .build();

// String oauth2UserId = "NAVER_testOauth2UserId";
// UserVo userVo = TestDataFactory.createTestUserVo(oauth2UserId);

// SignInResponseDto responseBody = SignInResponseDto.builder()
// .userType(UserType.GENERAL)
// .oauth2UserId(oauth2UserId)
// .expiresIn(Duration.ofMinutes(30))
// .clientId(ClientId.KAKAO).build();

// AuthTokenResponseDto responseHeader = AuthTokenResponseDto.builder()
// .accessToken("testAccessToken")
// .refreshToken("testRefreshToken")
// .build();

// Mockito.when(naverOAuthService.getOauth2UserId(requestDto)).thenReturn(oauth2UserId);
// Mockito.when(naverOAuthService.getUserVoFromOAuthToken(requestDto)).thenReturn(userVo);
// Mockito.when(naverOAuthService.handleNaverSignInBody(userVo,
// oauth2UserId)).thenReturn(responseBody);
// Mockito.when(oAuthService.handleSignInHeader(userVo,
// responseBody.getClientId())).thenReturn(responseHeader);
// Mockito.when(authService.createAccessTokenCookie(responseHeader.getAccessToken()))
// .thenReturn(ResponseCookie.from("accessToken",
// responseHeader.getAccessToken()).build());
// Mockito.when(authService.createRefreshTokenCookie(responseHeader.getRefreshToken()))
// .thenReturn(ResponseCookie.from("refreshToken",
// responseHeader.getRefreshToken()).build());

// // when & then
// mockMvc.perform(post("/oauth/naver/token")
// .contentType(MediaType.APPLICATION_JSON)
// .content(objectMapper.writeValueAsString(requestDto)))
// .andExpect(status().isOk())
// // .andExpect(header().exists("Set-Cookie"))
// // .andExpect(jsonPath("$.clientId").value(responseBody.getClientId()))
// // 여기에 필요한 추가적인 검증을 할 수 있습니다
// .andDo(document("/oauth/naver/token-signin",
// requestFields(
// fieldWithPath("authCode").description("인증 코드")),
// responseFields(
// fieldWithPath("expiresIn").description("엑세스 토큰 만료시간"),
// fieldWithPath("userType").description("유저 타입"),
// fieldWithPath("clientId").description("클라이언트 ID"),
// fieldWithPath("oauth2UserId").description("OAuth2 고유 아이디"))))
// .andDo(document("/oauth/naver/token",
// preprocessRequest(prettyPrint()),
// preprocessResponse(prettyPrint())));
// }

// }