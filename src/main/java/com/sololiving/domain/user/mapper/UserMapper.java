package com.sololiving.domain.user.mapper;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sololiving.domain.user.enums.Gender;
import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.vo.UserVo;

@Mapper
public interface UserMapper {
    // 회원가입
    void insertUser(UserVo userVo);

    // 회원탈퇴
    void deleteByUserId(@Param("userId") String userId);

    // 회원 마지막 로그인 시간 변경
    void updateUserLastSignInAt(@Param("userId") String userId);

    // 회원 상태 변경
    void updateUserStatus(@Param("userId") String userId, @Param("status") Status status);

    // 회원 이메일 변경
    void updateUserEmail(@Param("userId") String userId, @Param("email") String email);

    // 회원 이메일 변경
    void updateUserContact(@Param("userId") String userId, @Param("contact") String email);

    // 회원 닉네임 변경
    void updateUserNickname(@Param("userId") String userId, @Param("nickname") String email);

    // 회원 성별 변경
    void updateUserGender(@Param("userId") String userId, @Param("gender") Gender gender);

    // 회원 주소 변경
    void updateUserAddress(@Param("userId") String userId, @Param("address") String address);

    // 회원 생일 변경
    void updateUserBirth(@Param("userId") String userId, @Param("birth") LocalDate birth);

    // 회원 비밀번호 변경
    void updateUserPassword(@Param("userId") String userId, @Param("password") String password);
}
