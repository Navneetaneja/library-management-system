package com.project.library.controller;

import com.project.library.entity.User;
import com.project.library.models.WrapperResponse;
import com.project.library.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("create")
    public ResponseEntity<WrapperResponse<UUID>> createUser(@RequestBody User user) {
        UUID userId = userService.createUser(user);
        WrapperResponse<UUID> response = new WrapperResponse<>(true, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
