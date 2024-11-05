package com.sololiving.domain.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.comment.exception.CommentSuccessCode;
import com.sololiving.domain.comment.service.CommentLikeService;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.security.jwt.service.TokenProvider;
import com.sololiving.global.util.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentLikeController {

    private final TokenProvider tokenProvider;
    private final CookieService cookieService;
    private final UserAuthService userAuthService;
    private final CommentLikeService commentLikeService;

    // 댓글 추천
    @PostMapping("/{commentId}/like")
    public ResponseEntity<SuccessResponse> likeComment(@PathVariable Long commentId,
            HttpServletRequest httpServletRequest) {
        String userId = tokenProvider.getUserId(cookieService.extractAccessTokenFromCookie(httpServletRequest));
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        commentLikeService.likeComment(commentId, userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(CommentSuccessCode.SUCCESS_TO_LIKE_COMMENT));
    }

    // 댓글 추천 취소
    @DeleteMapping("{commentId}/like")
    public ResponseEntity<SuccessResponse> likeCommentCancle(@PathVariable Long commentId,
            HttpServletRequest httpServletRequest) {
        String userId = tokenProvider.getUserId(cookieService.extractAccessTokenFromCookie(httpServletRequest));
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        commentLikeService.likeCommentCancle(commentId, userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(CommentSuccessCode.SUCCESS_TO_CANCLE_LIKE_COMMENT));
    }
}
