package com.sololiving.domain.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.user.dto.response.ViewUserListResponseDto;
import com.sololiving.domain.user.service.UserViewService;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserViewController {

    private final UserViewService userViewService;
    private final CookieService cookieService;

    @GetMapping("/list")
    public ResponseEntity<List<ViewUserListResponseDto>> getUserList(HttpServletRequest httpServletRequest) {
        String accessToken = cookieService.extractAccessTokenFromCookie(httpServletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userViewService.viewUserList(accessToken));
    }

}
