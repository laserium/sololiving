package com.sololiving.domain.email.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sololiving.domain.email.exception.EmailErrorCode;
import com.sololiving.domain.email.mapper.EmailVerificationTokenMapper;
import com.sololiving.domain.email.vo.EmailVerificationTokenVo;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationTokenMapper emailVerificationTokenMapper;

    // CREATE
    @Transactional
    public String createVerificationToken(String userId, String newEmail) {
        String token = UUID.randomUUID().toString();
        EmailVerificationTokenVo emailVerificationTokenVo = EmailVerificationTokenVo.builder()
                .token(token)
                .userId(userId)
                .newEmail(newEmail)
                .expiresIn(LocalDateTime.now().plusMinutes(3))
                .build();
        emailVerificationTokenMapper.insertToken(emailVerificationTokenVo);
        return token;
    }

    // GET
    public EmailVerificationTokenVo getVerificationToken(String token) {
        EmailVerificationTokenVo emailVerificationTokenVo = emailVerificationTokenMapper.selectByToken(token);
        if (emailVerificationTokenVo != null) {
            return emailVerificationTokenVo;
        } else
            throw new ErrorException(EmailErrorCode.TOKEN_NOT_FOUND);
    }

    // DELETE
    @Transactional
    public void deleteVerificationToken(EmailVerificationTokenVo emailVerificationTokenVo) {
        emailVerificationTokenMapper.deleteToken(emailVerificationTokenVo);
    }
}
