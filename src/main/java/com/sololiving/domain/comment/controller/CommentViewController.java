package com.sololiving.domain.comment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.comment.dto.response.ViewCommentsResponseDto;
import com.sololiving.domain.comment.dto.response.ViewUsersCommentsResponseDto;
import com.sololiving.domain.comment.service.CommentViewService;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentViewController {

    private final CommentViewService commentViewService;

    // 게시물의 댓글 조회
    @GetMapping("")
    public ResponseEntity<List<ViewCommentsResponseDto>> viewComments(@RequestParam Long articleId,
            HttpServletRequest httpServletRequest) {
        String userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.OK).body(commentViewService.viewComments(articleId, userId));
    }

    // 사용자 댓글 조회
    @GetMapping("/{writer}")
    public ResponseEntity<List<ViewUsersCommentsResponseDto>> viewUserComments(
            @PathVariable String writer,
            @RequestParam(required = false) String searchContent,
            HttpServletRequest httpServletRequest) {

        String userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentViewService.viewUserComments(writer, userId, searchContent));
    }

}
