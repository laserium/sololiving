package com.sololiving.domain.user.vo;

import com.sololiving.domain.user.enums.Gender;
import com.sololiving.domain.user.enums.Status;
import com.sololiving.domain.user.enums.UserType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Getter
@Builder
public class UserVo implements UserDetails {

    private String userId;
    private String userPwd;
    private String oauth2UserId;
    private String nickname;
    private String contact;
    private String email;
    private Gender gender;
    private String address;
    private LocalDate birth;
    private Status status;
    private int followersCnt;
    private int followingCnt;
    private String profileImage;
    private String profileBio;
    private int points;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserType userType;
    private LocalDateTime lastSignInAt;
    private LocalDateTime lastActivityAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return authorities or roles, for simplicity returning an empty list here
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.userPwd;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Here you might want to use status or other field to determine this
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Here you might want to use status or other field to determine this
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // You may add additional logic here if necessary
        return true;
    }

    @Override
    public boolean isEnabled() {
        // If the user status should affect enabled state, check that here
        return this.status == Status.ACTIVE;
    }
}
