package com.sololiving.domain.block.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.block.service.BlockViewService;
import com.sololiving.global.security.jwt.service.TokenProvider;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/block")
public class BlockViewController {

    private final TokenProvider tokenProvider;
    private final CookieService cookieService;
    private final BlockViewService blockViewService;

    @GetMapping("/list")
    public ResponseEntity<?> viewBlockList(HttpServletRequest httpServletRequest) {
        String userId = tokenProvider.getUserId(cookieService.extractAccessTokenFromCookie(httpServletRequest));
        return ResponseEntity.status(HttpStatus.OK).body(blockViewService.viewBlockList(userId));
    }

}
