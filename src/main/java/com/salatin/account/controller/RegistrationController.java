package com.salatin.account.controller;

import com.salatin.account.model.dto.request.RegistrationRequestDto;
import com.salatin.account.model.dto.response.UserResponseDto;
import com.salatin.account.service.AuthService;
import com.salatin.account.service.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final AuthService authService;
    private final UserMapper userMapper;
    private final UsersResource usersResource;

    @PostMapping(value = "/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<UserResponseDto> register(
        @Valid @RequestBody RegistrationRequestDto request) {

        return userMapper.toMonoDto(authService.register(request), usersResource);
    }
}
