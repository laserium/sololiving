package com.sololiving.domain.email.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.sololiving.domain.email.dto.response.EmailResponseDto;
import com.sololiving.domain.user.service.UserAuthService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final UserAuthService userAuthService;
    private final EmailVerificationService emailVerificationService;

    @Value("${sololiving.app.url}")
    private String url;

    // 임시 비밀번호 설정 메일
    @Async("emailTaskExecutor")
    public void sendMailPasswordReset(EmailResponseDto emailResponseDto, String type) {
        String authNum = createCode();
        userAuthService.setTempPassword(emailResponseDto.getTo(), authNum);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            createMimeMessageHelper(mimeMessage, emailResponseDto.getTo(), emailResponseDto.getSubject(),
                    setContext(authNum, type));
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // 아이디 찾기 인증 메일
    @Async("emailTaskExecutor")
    public void sendMailIdRecover(String email, EmailResponseDto emailResponseDto, String type) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            createMimeMessageHelper(mimeMessage, emailResponseDto.getTo(),
                    emailResponseDto.getSubject(), setContext(userAuthService.findUserIdByEmail(email), type));
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // 회원 정보 수정 - 이메일 수정 인증 메일
    @Async("emailTaskExecutor")
    public void sendMailUpdateEmail(String email, String userId, EmailResponseDto emailResponseDto, String type) {
        try {
            String token = emailVerificationService.createVerificationToken(userId, email);
            String confirmationUrl = url + "/email/confirmation/" + token;
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            createMimeMessageHelper(mimeMessage, emailResponseDto.getTo(),
                    emailResponseDto.getSubject(), setContext(confirmationUrl, type));
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private MimeMessageHelper createMimeMessageHelper(MimeMessage mimeMessage, String to, String subject, String text)
            throws MessagingException {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, true);
        return mimeMessageHelper;
    }

    public String createCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0:
                    key.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                default:
                    key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }

    public String setContext(String data, String type) {
        Context context = new Context();
        context.setVariable("data", data);
        return templateEngine.process(type, context);
    }

}
