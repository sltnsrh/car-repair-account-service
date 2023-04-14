package com.salatin.account.controller;

import com.salatin.account.model.dto.request.RegistrationRequestDto;
import com.salatin.account.model.dto.response.UserResponseDto;
import com.salatin.account.service.UserService;
import com.salatin.account.service.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/res")
    public String resource() {
        return "Here your resource";
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserResponseDto> register(
        @Valid @RequestBody RegistrationRequestDto request) {

        return new ResponseEntity<>(userMapper.toDto(userService.create(request)),
            HttpStatus.CREATED);
    }
}
