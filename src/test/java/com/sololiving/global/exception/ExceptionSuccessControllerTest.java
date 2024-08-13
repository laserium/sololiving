package com.sololiving.global.exception;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import com.sololiving.global.config.AbstractRestDocsConfig;

public class ExceptionSuccessControllerTest extends AbstractRestDocsConfig {

    @Test
    @DisplayName("UserSccessCode 열거형 데이터 출력")
    void getUserSuccessCodes() throws Exception {
        ResultActions result = mockMvc.perform(get("/docs/success-codes/user")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andDo(document("success-codes/user",
                        responseFields(
                                fieldWithPath("[].code").type(JsonFieldType.STRING).description("에러 코드")
                                        .attributes(key("path").value("code")),
                                fieldWithPath("[].message").type(JsonFieldType.STRING).description("에러 메시지")
                                        .attributes(key("path").value("message")))))
                .andDo(document("success-codes/user",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("AuthSuccessCode 열거형 데이터 출력")
    void getAuthSuccessCodes() throws Exception {
        ResultActions result = mockMvc.perform(get("/docs/success-codes/auth")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andDo(document("success-codes/auth",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    // @Test
    // @DisplayName("TokenSuccessCode 열거형 데이터 출력")
    // void getTokenSuccessCodes() throws Exception {
    // ResultActions result = mockMvc.perform(get("/docs/success-codes/token")
    // .contentType(MediaType.APPLICATION_JSON));

    // result.andExpect(status().isOk())
    // .andDo(document("success-codes/token",
    // preprocessRequest(prettyPrint()),
    // preprocessResponse(prettyPrint())));
    // }

    @Test
    @DisplayName("EmailSuccessCode 열거형 데이터 출력")
    void getEmailSuccessCodes() throws Exception {
        ResultActions result = mockMvc.perform(get("/docs/success-codes/email")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andDo(document("success-codes/email",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

}
