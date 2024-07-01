// package com.sololiving.home.service;

// import com.sololiving.home.mapper.UserMapper;
// import com.sololiving.home.vo.UserVO;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.springframework.beans.factory.annotation.Autowired;
// import
// org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.junit.jupiter.SpringExtension;

// @ExtendWith(SpringExtension.class)
// @SpringBootTest
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// public class UserServiceTest {

// @Autowired
// private UserMapper userMapper;

// @InjectMocks
// private UserService userService;

// @Test
// void addUserTest() {
// UserVO userVO = new UserVO();
// userVO.setUserId("testuser");
// userVO.setUserPwd("1234");

// userService.addUser(userVO);
// }
// }
