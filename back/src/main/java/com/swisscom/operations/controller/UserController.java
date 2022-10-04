package com.swisscom.operations.controller;

import com.swisscom.operations.model.*;
import com.swisscom.operations.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/getAllUsers")
    public Result<List<User>, Failure> getAllUsers(@RequestParam int index){
        return userService.getAllUsers(index);
    }

    @PostMapping("/addUser")
    public Result<User, Failure> addUser(@RequestBody @Valid User user){
        return userService.addUser(user);
    }

    @PutMapping("/updateUser")
    public Result<UpdateResponse, Failure> updateUser(@RequestBody Map<String, Object> updates){
        return userService.updateUser(updates);
    }

    @DeleteMapping("/deleteUser")
    public Result<DeleteResponse, Failure> deleteUser(@RequestParam String userId){
        return userService.deleteUser(userId);
    }

    @PostMapping("/loginUser")
    public Result<User, Failure> loginUser(@RequestBody @Valid Credentials credentials){
        return userService.login(credentials);
    }
}
