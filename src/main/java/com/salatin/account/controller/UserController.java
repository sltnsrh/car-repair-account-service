package com.salatin.account.controller;

import com.salatin.account.model.dto.response.UserResponseDto;
import com.salatin.account.service.UserService;
import com.salatin.account.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final UsersResource usersResource;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getInfo(@PathVariable String id) {

        return new ResponseEntity<>(
            userMapper.toDto(
                userService.findById(id), usersResource),
            HttpStatus.OK);
    }
}
