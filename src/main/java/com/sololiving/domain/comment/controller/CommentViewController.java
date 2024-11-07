package com.sololiving.domain.comment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.domain.comment.dto.response.ViewCommentsResponseDto;
import com.sololiving.domain.comment.service.CommentViewService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentViewController {

    private final CommentViewService commentViewService;

    // 해당 게시물의 댓글 전부 조회
    @GetMapping("")
    public ResponseEntity<List<ViewCommentsResponseDto>> viewComments(@RequestParam Long articleId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentViewService.viewComments(articleId));
    }

}
