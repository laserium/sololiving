package com.sololiving.domain.user.controller;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
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

    @Test
    void testGetUserList() throws Exception {

        ViewUserListResponseDto user1 = ViewUserListResponseDto.builder()
                .userId("user1")
                .email("user1@example.com")
                .contact("01012345678")
                .status(Status.ACTIVE)
                .userType(UserType.GENERAL)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lastSignInAt(null)
                .lastActivityAt(LocalDateTime.now())
                .build();
        ViewUserListResponseDto user2 = ViewUserListResponseDto.builder()
                .userId("user2")
                .email("user2@example.com")
                .contact("01022222222")
                .status(Status.ACTIVE)
                .userType(UserType.GENERAL)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lastSignInAt(null)
                .lastActivityAt(LocalDateTime.now())
                .build();

        Mockito.when(userViewService.viewUserList(Mockito.anyString()))
                .thenReturn(Arrays.asList(user1, user2));

        Mockito.when(cookieService.extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class)))
                .thenReturn("testAccessToken");

        mockMvc.perform(get("/users/list")
                .header("Set-Cookie", "accessToken=testAccessToken"))
                .andExpect(status().isOk())
                .andDo(document("users/view/users-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Set-Cookie").description("accessToken = 엑세스토큰"))));
    }
}
