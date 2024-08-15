package com.sololiving.domain.user.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.sololiving.domain.auth.dto.auth.request.SignUpRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestDto.UpdateUserAddressRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestDto.UpdateUserBirthRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestDto.UpdateUserContactRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestDto.UpdateUserEmailRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestDto.UpdateUserGenderRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestDto.UpdateUserNicknameRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestDto.UpdateUserPasswordRequestDto;
import com.sololiving.domain.user.dto.request.UpdateUserRequestDto.ValidateUpdateUserContactRequestDto;
import com.sololiving.domain.user.enums.Gender;
import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.exception.UserSuccessCode;
import com.sololiving.domain.user.service.UserService;
import com.sololiving.global.config.AbstractRestDocsConfig;
import com.sololiving.global.security.sms.exception.SmsSuccessCode;
import com.sololiving.global.security.sms.service.SmsService;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;

public class UserControllerTest extends AbstractRestDocsConfig {

    @MockBean
    private UserService userService;

    @MockBean
    private CookieService cookieService;

    @MockBean
    private SmsService smsService;

    @Test
    @DisplayName("회원가입")
    void createUserTest() throws Exception {

        // given
        SignUpRequestDto requestDto = SignUpRequestDto.builder()
                .userId("testUser001")
                .userPwd("1234")
                .oauth2UserId(null)
                .contact("01088889999")
                .email("testUser001@naver.com")
                .build();

        // when & then
        mockMvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andDo(document("/users/signup",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").description("회원 아이디"),
                                fieldWithPath("userPwd").description("회원 비밀번호"),
                                fieldWithPath("oauth2UserId").description("회원 OAuth2 고유 아이디")
                                        .attributes(key("constraint")
                                                .value("prefix => 네이버 : NAVER_ / 카카오 : KAKAO_ / 구글 : GOOGLE_")),
                                fieldWithPath("contact").description("회원 연락처"),
                                fieldWithPath("email").description("회원 이메일"))));

        // signUp 메서드가 1회 호출되었는지 검증
        Mockito.verify(userService, Mockito.times(1)).signUp(Mockito.any(SignUpRequestDto.class));
    }

    @Test
    @DisplayName("회원탈퇴 테스트")
    void deleteUserTest() throws Exception {
        // given
        String accessToken = "testAccessToken";

        // mocking
        Mockito.when(cookieService.extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class)))
                .thenReturn(accessToken);
        Mockito.doNothing().when(userService).deleteUserRequest(accessToken);

        // when & then
        mockMvc.perform(delete("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Set-Cookie", "accessToken=" + accessToken)) // 테스트용 쿠키 설정
                .andExpect(status().isOk())
                .andDo(document("/users/delete/user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Set-Cookie").description("accessToken=" + accessToken))));

        // verify
        Mockito.verify(cookieService, Mockito.times(1))
                .extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class)); // 액세스 토큰 추출 확인
        Mockito.verify(userService, Mockito.times(1))
                .deleteUserRequest(accessToken); // UserService의 탈퇴 요청이 1회 호출되었는지 확인
    }

    @Test
    @DisplayName("상태 변경 - ACTIVE 테스트")
    void updateUserStatusActive() throws Exception {
        // given
        String accessToken = "testAccessToken";
        Status status = Status.ACTIVE;

        // mocking
        Mockito.when(cookieService.extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class)))
                .thenReturn(accessToken);
        Mockito.doNothing().when(userService).updateStatus(accessToken, status);

        // when & then
        mockMvc.perform(patch("/users/status/{status}", status)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Set-Cookie", "accessToken=" + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(UserSuccessCode.USER_STATUS_ACTIVE.getCode()))
                .andExpect(jsonPath("$.message").value(UserSuccessCode.USER_STATUS_ACTIVE.getMessage()))
                .andDo(document("/users/update/status-active",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Set-Cookie").description("accessToken = 엑세스토큰")),
                        pathParameters(
                                parameterWithName("status").description("변경할 유저 상태 (ACTIVE, BLOCKED, WITHDRAWN)"))));

        // verify
        Mockito.verify(cookieService, Mockito.times(1))
                .extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class));
        Mockito.verify(userService, Mockito.times(1)).updateStatus(accessToken, status);
    }

    @Test
    @DisplayName("상태 변경 - BLOCKED 테스트")
    void updateUserStatusBlocked() throws Exception {
        // given
        String accessToken = "testAccessToken";
        Status status = Status.BLOCKED;

        // mocking
        Mockito.when(cookieService.extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class)))
                .thenReturn(accessToken);
        Mockito.doNothing().when(userService).updateStatus(accessToken, status);

        // when & then
        mockMvc.perform(patch("/users/status/{status}", status)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Set-Cookie", "accessToken=" + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(UserSuccessCode.USER_STATUS_BLOCKED.getCode()))
                .andExpect(jsonPath("$.message").value(UserSuccessCode.USER_STATUS_BLOCKED.getMessage()))
                .andDo(document("/users/update/status-blocked",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Set-Cookie").description("accessToken = 엑세스토큰")),
                        pathParameters(
                                parameterWithName("status").description("변경할 유저 상태 (ACTIVE, BLOCKED, WITHDRAWN)"))));

        // verify
        Mockito.verify(cookieService, Mockito.times(1))
                .extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class));
        Mockito.verify(userService, Mockito.times(1)).updateStatus(accessToken, status);
    }

    @Test
    @DisplayName("상태 변경 - WITHDRAWN 테스트")
    void updateUserStatusWithdrawn() throws Exception {
        // given
        String accessToken = "testAccessToken";
        Status status = Status.WITHDRAWN;

        // mocking
        Mockito.when(cookieService.extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class)))
                .thenReturn(accessToken);
        Mockito.doNothing().when(userService).updateStatus(accessToken, status);

        // when & then
        mockMvc.perform(patch("/users/status/{status}", status)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Set-Cookie", "accessToken=" + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(UserSuccessCode.USER_STATUS_WITHDRAWN.getCode()))
                .andExpect(jsonPath("$.message").value(UserSuccessCode.USER_STATUS_WITHDRAWN.getMessage()))
                .andDo(document("/users/update/status-withdrawn",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Set-Cookie").description("accessToken = 엑세스토큰")),
                        pathParameters(
                                parameterWithName("status").description("변경할 유저 상태 (ACTIVE, BLOCKED, WITHDRAWN)"))));

        // verify
        Mockito.verify(cookieService, Mockito.times(1))
                .extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class));
        Mockito.verify(userService, Mockito.times(1)).updateStatus(accessToken, status);
    }

    @Test
    @DisplayName("회원 연락처 변경 전 인증 메일 전송 테스트")
    void validateUpdateUserContactTest() throws Exception {
        // given
        String accessToken = "testAccessToken";
        ValidateUpdateUserContactRequestDto requestDto = ValidateUpdateUserContactRequestDto.builder()
                .contact("01012345678")
                .build();

        // mocking
        Mockito.when(cookieService.extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class)))
                .thenReturn(accessToken);
        Mockito.when(userService.validateUpdateUserContact(accessToken, requestDto))
                .thenReturn("testVerificationCode");

        // when & then
        mockMvc.perform(post("/users/contact/sms-verification")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Set-Cookie", "accessToken=" + accessToken)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SmsSuccessCode.SUCCESS_TO_SEND.getCode()))
                .andExpect(jsonPath("$.message").value(SmsSuccessCode.SUCCESS_TO_SEND.getMessage()))
                .andDo(document("/users/update/contact/post-sms-verification",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Set-Cookie").description("accessToken = 엑세스토큰")),
                        requestFields(
                                fieldWithPath("contact").description("유저 연락처"))));

        // verify
        Mockito.verify(cookieService, Mockito.times(1))
                .extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class));
        Mockito.verify(userService, Mockito.times(1))
                .validateUpdateUserContact(Mockito.eq(accessToken), Mockito.any());
    }

    @Test
    @DisplayName("회원 연락처 변경 테스트")
    void updateUserContactTest() throws Exception {
        // given
        String accessToken = "testAccessToken";
        UpdateUserContactRequestDto requestDto = UpdateUserContactRequestDto.builder()
                .code("testVerificationCode")
                .contact("01098765432")
                .build();

        // mocking
        Mockito.when(cookieService.extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class)))
                .thenReturn(accessToken);
        Mockito.doNothing().when(userService).updateUserContact(accessToken, requestDto);

        // when & then
        mockMvc.perform(patch("/users/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Set-Cookie", "accessToken=" + accessToken)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS.getMessage()))
                .andDo(document("/users/update/contact",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Set-Cookie").description("accessToken = 엑세스토큰")),
                        requestFields(
                                fieldWithPath("code").description("문자(SMS)로 받은 인증 코드"),
                                fieldWithPath("contact").description("유저 연락처"))));

        // verify
        Mockito.verify(cookieService, Mockito.times(1))
                .extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class));
        Mockito.verify(userService, Mockito.times(1))
                .updateUserContact(Mockito.eq(accessToken), Mockito.any());
    }

    @Test
    @DisplayName("회원 이메일 변경 테스트")
    void updateUserEmailTest() throws Exception {
        // given
        String accessToken = "testAccessToken";
        UpdateUserEmailRequestDto requestDto = UpdateUserEmailRequestDto.builder()
                .email("testUseremail@naver.com")
                .build();

        // mocking
        Mockito.when(cookieService.extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class)))
                .thenReturn(accessToken);
        Mockito.doNothing().when(userService).sendUpdateNewEmailRequest(accessToken, requestDto);

        // when & then
        mockMvc.perform(patch("/users/email")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Set-Cookie", "accessToken=" + accessToken)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(UserSuccessCode.UPDATE_EMAIL_REQUEST_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(UserSuccessCode.UPDATE_EMAIL_REQUEST_SUCCESS.getMessage()))
                .andDo(document("/users/update/email",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Set-Cookie").description("accessToken = 엑세스토큰")),
                        requestFields(
                                fieldWithPath("email").description("변경할 새 이메일"))));

        // verify
        Mockito.verify(cookieService, Mockito.times(1))
                .extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class));
        Mockito.verify(userService, Mockito.times(1))
                .sendUpdateNewEmailRequest(Mockito.eq(accessToken), Mockito.any());
    }

    @Test
    @DisplayName("회원 비밀번호 변경 테스트")
    void updateUserPasswordTest() throws Exception {
        // given
        String accessToken = "testAccessToken";
        UpdateUserPasswordRequestDto requestDto = UpdateUserPasswordRequestDto.builder()
                .password("testUserPassword")
                .newPassword("testUserNewPassword")
                .build();

        // mocking
        Mockito.when(cookieService.extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class)))
                .thenReturn(accessToken);
        Mockito.doNothing().when(userService).updateUserPassword(accessToken, requestDto);

        // when & then
        mockMvc.perform(patch("/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Set-Cookie", "accessToken=" + accessToken)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS.getMessage()))
                .andDo(document("/users/update/password",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Set-Cookie").description("accessToken = 엑세스토큰")),
                        requestFields(
                                fieldWithPath("password").description("기존 비밀번호"),
                                fieldWithPath("newPassword").description("변경할 새 비밀번호"))));

        // verify
        Mockito.verify(cookieService, Mockito.times(1))
                .extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class));
        Mockito.verify(userService, Mockito.times(1))
                .updateUserPassword(Mockito.eq(accessToken), Mockito.any());
    }

    @Test
    @DisplayName("회원 닉네임 변경 테스트")
    void updateUserNicknameTest() throws Exception {
        // given
        String accessToken = "testAccessToken";
        UpdateUserNicknameRequestDto requestDto = UpdateUserNicknameRequestDto.builder()
                .nickname("testUserNickname")
                .build();

        // mocking
        Mockito.when(cookieService.extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class)))
                .thenReturn(accessToken);
        Mockito.doNothing().when(userService).updateUserNickname(accessToken, requestDto);

        // when & then
        mockMvc.perform(patch("/users/nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Set-Cookie", "accessToken=" + accessToken)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS.getMessage()))
                .andDo(document("/users/update/nickname",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Set-Cookie").description("accessToken = 엑세스토큰")),
                        requestFields(
                                fieldWithPath("nickname").description("변경할 닉네임"))));

        // verify
        Mockito.verify(cookieService, Mockito.times(1))
                .extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class));
        Mockito.verify(userService, Mockito.times(1))
                .updateUserNickname(Mockito.eq(accessToken), Mockito.any());
    }

    @Test
    @DisplayName("회원 성별 변경 테스트")
    void updateUserGenderTest() throws Exception {
        // given
        String accessToken = "testAccessToken";
        UpdateUserGenderRequestDto requestDto = UpdateUserGenderRequestDto.builder()
                .gender(Gender.FEMALE)
                .build();

        // mocking
        Mockito.when(cookieService.extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class)))
                .thenReturn(accessToken);
        Mockito.doNothing().when(userService).updateUserGender(accessToken, requestDto);

        // when & then
        mockMvc.perform(patch("/users/gender")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Set-Cookie", "accessToken=" + accessToken)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS.getMessage()))
                .andDo(document("/users/update/gender",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Set-Cookie").description("accessToken = 엑세스토큰")),
                        requestFields(
                                fieldWithPath("gender").description("변경할 성별"))));

        // verify
        Mockito.verify(cookieService, Mockito.times(1))
                .extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class));
        Mockito.verify(userService, Mockito.times(1))
                .updateUserGender(Mockito.eq(accessToken), Mockito.any());
    }

    @Test
    @DisplayName("회원 주소 변경 테스트")
    void updateUserAddressTest() throws Exception {
        // given
        String accessToken = "testAccessToken";
        UpdateUserAddressRequestDto requestDto = UpdateUserAddressRequestDto.builder()
                .address("test's_home")
                .build();

        // mocking
        Mockito.when(cookieService.extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class)))
                .thenReturn(accessToken);
        Mockito.doNothing().when(userService).updateUserAddress(accessToken, requestDto);

        // when & then
        mockMvc.perform(patch("/users/address")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Set-Cookie", "accessToken=" + accessToken)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS.getMessage()))
                .andDo(document("/users/update/address",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Set-Cookie").description("accessToken = 엑세스토큰")),
                        requestFields(
                                fieldWithPath("address").description("변경할 주소 정보"))));

        // verify
        Mockito.verify(cookieService, Mockito.times(1))
                .extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class));
        Mockito.verify(userService, Mockito.times(1))
                .updateUserAddress(Mockito.eq(accessToken), Mockito.any());
    }

    @Test
    @DisplayName("회원 생일 변경 테스트")
    void updateUserBirthTest() throws Exception {
        // given
        String accessToken = "testAccessToken";
        UpdateUserBirthRequestDto requestDto = UpdateUserBirthRequestDto.builder()
                .birth(LocalDate.of(1999, 01, 01))
                .build();

        // mocking
        Mockito.when(cookieService.extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class)))
                .thenReturn(accessToken);
        Mockito.doNothing().when(userService).updateUserBirth(accessToken, requestDto);

        // when & then
        mockMvc.perform(patch("/users/birth")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Set-Cookie", "accessToken=" + accessToken)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(UserSuccessCode.UPDATE_USER_REQUEST_SUCCESS.getMessage()))
                .andDo(document("/users/update/birth",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName("Set-Cookie").description("accessToken = 엑세스토큰")),
                        requestFields(
                                fieldWithPath("birth").description("변경할 생일 정보"))));

        // verify
        Mockito.verify(cookieService, Mockito.times(1))
                .extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class));
        Mockito.verify(userService, Mockito.times(1))
                .updateUserBirth(Mockito.eq(accessToken), Mockito.any());
    }

}
