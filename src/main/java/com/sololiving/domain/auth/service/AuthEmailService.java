package com.sololiving.domain.auth.service;

import java.util.Random;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.sololiving.domain.auth.dto.email.response.EmailResponseDto;
import com.sololiving.domain.user.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthEmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final UserService userService;

    @Async("emailTaskExecutor")
    public void sendMailPasswordReset(EmailResponseDto emailResponseDto, String type) {
        String authNum = createCode();
        userService.setTempPassword(emailResponseDto.getTo(), authNum);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            createMimeMessageHelper(mimeMessage, emailResponseDto.getTo(), emailResponseDto.getSubject(),
                    setContext(authNum, type));
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("emailTaskExecutor")
    public void sendMailIdRecover(String email, EmailResponseDto emailResponseDto, String type) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            createMimeMessageHelper(mimeMessage, emailResponseDto.getTo(),
                    emailResponseDto.getSubject(), setContext(userService.findUserIdByEmail(email), type));
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

    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }

}
