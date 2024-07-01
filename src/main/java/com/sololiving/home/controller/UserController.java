package com.sololiving.home.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sololiving.home.dto.UserDto.SignUpRequest;
import com.sololiving.home.service.UserService;
import com.sololiving.home.vo.UserVO;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        UserVO userVO = new UserVO(signUpRequest.getUserId(), signUpRequest.getUserPwd());
        userService.addUser(userVO);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");
    }

}
