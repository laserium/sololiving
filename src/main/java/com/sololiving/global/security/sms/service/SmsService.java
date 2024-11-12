package com.sololiving.global.security.sms.service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService {

    private DefaultMessageService messageService;
    private final SmsRedisService smsRedisService;

    @Value("${sololiving.coolsms.api.key}")
    private String apiKey;

    @Value("${sololiving.coolsms.api.secret}")
    private String apiSecret;

    @Value("${sololiving.coolsms.fromnumber}")
    private String fromNumber;

    private String makeRandomNumber() {
        Random rand = new Random();
        String randomNum = "";
        for (int i = 0; i < 6; i++) {
            String random = Integer.toString(rand.nextInt(10));
            randomNum += random;
        }

        return randomNum;
    }

    @Async("smsTaskExecutor")
    public CompletableFuture<SingleMessageSentResponse> sendSms(String phone) {
        CompletableFuture<SingleMessageSentResponse> future = new CompletableFuture<>();
        try {
            this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
            Message message = new Message();
            String randomNum = makeRandomNumber();
            message.setFrom(fromNumber);
            message.setTo(phone);
            message.setText("[홀로서기] 인증 번호입니다." + " [" + randomNum + "] \n 유효시간 안에 입력해주세요.");
            SingleMessageSentResponse result = this.messageService.sendOne(new SingleMessageSendingRequest(message));
            smsRedisService.createSmsCertification(phone, randomNum);
            future.complete(result); // 비동기 작업 완료 및 결과 설정
        } catch (Exception e) {
            future.completeExceptionally(e); // 예외 발생 시 처리
        }
        return future;
    }

    // 인증번호 확인
    public boolean checkSms(String phone, String code) {
        String savedCertificationNumber = smsRedisService.getSmsCertification(phone);
        if (savedCertificationNumber != null && savedCertificationNumber.equals(code)) {
            // 인증번호가 일치하면 Redis에서 삭제
            smsRedisService.deleteSmsCertification(phone);
            return true;
        }
        return false;
    }
}
