package com.swisscom.operations.controller;

import com.swisscom.operations.model.*;
import com.swisscom.operations.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @InjectMocks
    UserController subject;

    @Mock
    IUserService userService;

    User output;

    User input;
    @BeforeEach
    void init(){
        output = User.builder().id(UUID.randomUUID().toString()).build();
        input = User.builder()
                .name("test")
                .email("test@test.com")
                .password("123456")
                .roles("ADMIN")
                .build();
    }

    @Test
    void getAllUsersTest(){
        when(userService.getAllUsers(anyInt()))
                .thenReturn(Result.success(List.of(output)));
        assertNotNull(subject.getAllUsers(0));
    }

    @Test
    void addUserTest(){
        when(userService.addUser(any())).thenReturn(Result.success(output));
        assertNotNull(subject.addUser(input));
    }

    @Test
    void updateUserTest(){
        when(userService.updateUser(anyMap()))
                .thenReturn(Result.success(new UpdateResponse(1)));
        assertNotNull(subject.updateUser(Map.of("name", "ABC")));
    }

    @Test
    void deleteUserTest(){
        when(userService.deleteUser(anyString()))
                .thenReturn(Result.success(new DeleteResponse(1)));
        assertNotNull(subject.deleteUser(UUID.randomUUID().toString()));
    }

    @Test
    void loginUserTest(){
        when(userService.login(any()))
                .thenReturn(Result.success(output));
        assertNotNull(subject.loginUser(Credentials.builder().email("abc@test.com").password("123456").build()));
    }
}
