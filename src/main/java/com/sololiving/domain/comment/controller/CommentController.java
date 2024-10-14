package com.sololiving.domain.comment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.comment.dto.request.CreateCommentRequestDto;
import com.sololiving.domain.comment.service.CommentService;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.security.jwt.service.TokenProvider;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final CookieService cookieService;
    private final TokenProvider tokenProvider;
    private final UserAuthService userAuthService;

    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody CreateCommentRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        String userId = tokenProvider.getUserId(cookieService.extractAccessTokenFromCookie(httpServletRequest));
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
