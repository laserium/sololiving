package com.sololiving.domain.email.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.email.vo.EmailVerificationTokenVo;

@Mapper
public interface EmailVerificationTokenMapper {
    EmailVerificationTokenVo selectByToken(@Param("token") String token);

    void insertToken(EmailVerificationTokenVo emailVerificationTokenVo);

    void deleteToken(EmailVerificationTokenVo emailVerificationTokenVo);
}
