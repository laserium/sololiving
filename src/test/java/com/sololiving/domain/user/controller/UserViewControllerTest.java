package com.sololiving.domain.user.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;

import com.sololiving.domain.user.dto.response.ViewUserListResponseDto;
import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.enums.UserType;
import com.sololiving.domain.user.service.UserViewService;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.time.LocalDateTime;

@WebMvcTest(UserViewController.class)
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
public class UserViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserViewService userViewService;

    @MockBean
    private CookieService cookieService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(
                        document("{method-name}", preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())))
                .build();
    }

    @Test
    void testGetUserList() throws Exception {

        // Mocking the service response
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

        // Mocking CookieService to return dummy access token
        Mockito.when(cookieService.extractAccessTokenFromCookie(Mockito.any(HttpServletRequest.class)))
                .thenReturn("dummyAccessToken");

        this.mockMvc.perform(get("/users/list")
                .header("Cookie", "accessToken=dummyAccessToken"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("users-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Cookie").description(
                                        "세션 식별을 위한 쿠키. 액세스 토큰이 포함됩니다."))));
    }
}
