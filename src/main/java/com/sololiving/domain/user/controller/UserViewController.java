package com.sololiving.domain.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.user.dto.response.ViewUserListResponseDto;
import com.sololiving.domain.user.dto.response.ViewUserProfileResponseDto;
import com.sololiving.domain.user.service.UserViewService;
import com.sololiving.global.util.CookieService;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserViewController {

    private final UserViewService userViewService;

}