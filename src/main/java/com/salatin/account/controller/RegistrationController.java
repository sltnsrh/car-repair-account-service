package com.salatin.account.controller;

import com.salatin.account.model.dto.User;
import com.salatin.account.model.dto.request.RegistrationRequestDto;
import com.salatin.account.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;

    @GetMapping("/res")
    public String resource() {
        return "Here your resource";
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Mono<User>> register(
        @Valid @RequestBody RegistrationRequestDto request) {

        return new ResponseEntity<>(userService.create(request), HttpStatus.CREATED);
    }
}
