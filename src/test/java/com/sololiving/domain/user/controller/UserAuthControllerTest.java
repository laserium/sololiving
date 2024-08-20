package com.sololiving.domain.user.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.sololiving.domain.user.dto.request.SignUpVerificationSmsRequestDto.CheckSignUpVerificationSmsRequestDto;
import com.sololiving.domain.user.dto.request.SignUpVerificationSmsRequestDto.SendSignUpVerificationSmsRequestDto;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.config.AbstractRestDocsConfig;
import com.sololiving.global.security.sms.exception.SmsSuccessCode;
import com.sololiving.global.security.sms.service.SmsService;

public class UserAuthControllerTest extends AbstractRestDocsConfig {

    @MockBean
    private UserAuthService userAuthService;

    @MockBean
    private SmsService smsService;

    // @Test
    // @DisplayName("회원가입 시 휴대폰 인증번호 전송 테스트")
    // void sendSignUpVerificationSmsTest() throws Exception {
    //     // given
    //     SendSignUpVerificationSmsRequestDto requestDto = SendSignUpVerificationSmsRequestDto.builder()
    //             .contact("01011112222")
    //             .build();

    //     // mocking
    //     Mockito.doNothing().when(userAuthService)
    //             .sendSignUpVerificationSms(Mockito.any(SendSignUpVerificationSmsRequestDto.class));

    //     // when & then
    //     mockMvc.perform(post("/users/auth/contact-verification/send")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(requestDto)))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.code").value(SmsSuccessCode.SUCCESS_TO_SEND.getCode()))
    //             .andExpect(jsonPath("$.message").value(SmsSuccessCode.SUCCESS_TO_SEND.getMessage()))
    //             .andDo(document("/users/auth/signup-contact-verification/send",
    //                     preprocessRequest(prettyPrint()),
    //                     preprocessResponse(prettyPrint()),
    //                     requestFields(
    //                             fieldWithPath("contact").description("휴대폰 번호"))))
    //             .andDo(print()); // 디버깅용 로그 출력

    //     // verify
    //     Mockito.verify(userAuthService, Mockito.times(1))
    //             .sendSignUpVerificationSms(Mockito.any(SendSignUpVerificationSmsRequestDto.class));
    // }

    @Test
    @DisplayName("회원가입 시 휴대폰 인증번호 검증 테스트")
    void checkSignUpVerificationCodeTest() throws Exception {
        // given
        CheckSignUpVerificationSmsRequestDto requestDto = CheckSignUpVerificationSmsRequestDto.builder()
                .contact("01012345678")
                .code("123456")
                .build();

        // mocking
        Mockito.when(smsService.checkSms(requestDto.getContact(), requestDto.getCode())).thenReturn(true);

        // when & then
        mockMvc.perform(post("/users/auth/contact-verification/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SmsSuccessCode.CERTIFICATION_NUMBER_CORRECT.getCode()))
                .andExpect(jsonPath("$.message").value(SmsSuccessCode.CERTIFICATION_NUMBER_CORRECT.getMessage()))
                .andDo(document("/users/auth/signup-contact-verification/check",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("contact").description("휴대폰 번호"),
                                fieldWithPath("code").description("문자로 전송한 코드 6자리"))));

        // verify
        Mockito.verify(smsService, Mockito.times(1)).checkSms(Mockito.eq(requestDto.getContact()),
                Mockito.eq(requestDto.getCode()));
    }

}
