package com.salatin.account.controller;

import com.salatin.account.model.dto.request.RegistrationRequestDto;
import com.salatin.account.model.dto.response.UserResponseDto;
import com.salatin.account.service.AuthService;
import com.salatin.account.service.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final AuthService authService;
    private final UserMapper userMapper;
    private final UsersResource usersResource;

    @PostMapping(value = "/register")
    public ResponseEntity<UserResponseDto> register(
        @Valid @RequestBody RegistrationRequestDto request) {

        return new ResponseEntity<>(userMapper.toDto(authService.register(request), usersResource),
            HttpStatus.CREATED);
    }
}
