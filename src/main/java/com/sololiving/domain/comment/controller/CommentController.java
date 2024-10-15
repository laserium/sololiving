package com.sololiving.domain.comment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.comment.dto.request.CreateCommentRequestDto;
import com.sololiving.domain.comment.dto.request.CreateReCommentRequestDto;
import com.sololiving.domain.comment.exception.CommentSuccessCode;
import com.sololiving.domain.comment.service.CommentService;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.ResponseMessage;
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

    // 댓글 작성
    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody CreateCommentRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        String userId = tokenProvider.getUserId(cookieService.extractAccessTokenFromCookie(httpServletRequest));
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        commentService.addComment(requestDto, userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(CommentSuccessCode.SUCCESS_TO_POST_COMMENT));
    }

    // 대댓글 작성
    @PostMapping("path")
    public ResponseEntity<?> addReComment(@RequestBody CreateReCommentRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        String userId = tokenProvider.getUserId(cookieService.extractAccessTokenFromCookie(httpServletRequest));
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        commentService.addReComment(requestDto, userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(CommentSuccessCode.SUCCESS_TO_POST_COMMENT));

    }

}
