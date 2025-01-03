package com.sololiving.domain.comment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.comment.dto.request.AddCommentRequestDto;
import com.sololiving.domain.comment.dto.request.AddReCommentRequestDto;
import com.sololiving.domain.comment.dto.request.DeleteCommentRequestDto;
import com.sololiving.domain.comment.dto.request.UpdateCommentRequestDto;
import com.sololiving.domain.comment.exception.CommentSuccessCode;
import com.sololiving.domain.comment.service.CommentService;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.ResponseMessage;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.exception.success.SuccessResponse;
import com.sololiving.global.util.IpAddressUtil;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final UserAuthService userAuthService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<SuccessResponse> addComment(@RequestBody AddCommentRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        commentService.addComment(requestDto, userId, IpAddressUtil.getClientIp(httpServletRequest));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseMessage.createSuccessResponse(CommentSuccessCode.SUCCESS_TO_POST_COMMENT));
    }

    // 대댓글 작성
    @PostMapping("/re")
    public ResponseEntity<SuccessResponse> addReComment(@RequestBody AddReCommentRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        commentService.addReComment(requestDto, userId, IpAddressUtil.getClientIp(httpServletRequest));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseMessage.createSuccessResponse(CommentSuccessCode.SUCCESS_TO_POST_COMMENT));

    }

    // 댓글 삭제
    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> removeComment(@RequestBody DeleteCommentRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        commentService.removeComment(requestDto.getCommentId(), userId, IpAddressUtil.getClientIp(httpServletRequest));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(CommentSuccessCode.SUCCESS_TO_DELETE_COMMENT));

    }

    // 댓글 수정
    @PatchMapping("{commentId}")
    public ResponseEntity<SuccessResponse> updateComment(@PathVariable Long commentId,
            @RequestBody UpdateCommentRequestDto requestDto,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        commentService.modifyComment(commentId, userId, requestDto, IpAddressUtil.getClientIp(httpServletRequest));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.createSuccessResponse(CommentSuccessCode.SUCCESS_TO_UPDATE_COMMENT));

    }
}
