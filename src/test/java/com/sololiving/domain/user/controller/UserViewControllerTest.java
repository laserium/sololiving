package com.sololiving.domain.user.controller;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sololiving.domain.user.dto.response.ViewUserListResponseDto;
import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.service.UserViewService;
import com.sololiving.global.config.AbstractRestDocsConfig;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;

public class UserViewControllerTest extends AbstractRestDocsConfig {

    @MockBean
    private UserViewService userViewService;

    @MockBean
    private CookieService cookieService;

    // @Test
    // void testGetUserList() throws Exception {

    // // given
    // String accessToken = "testAccessToken";
    // ViewUserListResponseDto user1 = ViewUserListResponseDto.builder()
    // .userId("user1")
    // .email("user1@example.com")
    // .contact("01012345678")
    // .status(Status.ACTIVE)
    // .userType(UserType.GENERAL)
    // .createdAt(LocalDateTime.now())
    // .updatedAt(LocalDateTime.now())
    // .lastSignInAt(null)
    // .lastActivityAt(LocalDateTime.now())
    // .build();
    // ViewUserListResponseDto user2 = ViewUserListResponseDto.builder()
    // .userId("user2")
    // .email("user2@example.com")
    // .contact("01022222222")
    // .status(Status.ACTIVE)
    // .userType(UserType.GENERAL)
    // .createdAt(LocalDateTime.now())
    // .updatedAt(LocalDateTime.now())
    // .lastSignInAt(null)
    // .lastActivityAt(LocalDateTime.now())
    // .build();

    // // mocking
    // Mockito.when(userViewService.viewUserList(Mockito.anyString()))
    // .thenReturn(Arrays.asList(user1, user2));
    // Mockito.when(cookieService.extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class)))
    // .thenReturn(accessToken);

    // // when & then
    // mockMvc.perform(get("/users/list")
    // .header("Set-Cookie", "accessToken=" + accessToken))
    // .andExpect(status().isOk())
    // .andDo(document("users/view/users-list",
    // preprocessRequest(prettyPrint()),
    // preprocessResponse(prettyPrint()),
    // requestHeaders(
    // headerWithName("Set-Cookie").description("accessToken = 엑세스토큰")),
    // responseFields(
    // fieldWithPath("userId").description("사용자 아이디"),
    // fieldWithPath("email").description("사용자 이메일"),
    // fieldWithPath("contact").description("사용자 연락처"),
    // fieldWithPath("status").description("사용자 상태"),
    // fieldWithPath("userType").description("사용자 타입"),
    // fieldWithPath("createdAt").description("사용자가 회원가입한 날짜"),
    // fieldWithPath("updatedAt").description("최근 사용자 정보 수정 날짜 및 시간"),
    // fieldWithPath("lastSignInAt").description("사용자의 최근 로그인 날짜 및 시간"),
    // fieldWithPath("lastActivityAt").description("사용자의 최근 활동 날짜 및 시간"))));
    // }
}
