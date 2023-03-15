package com.salatin.account.controller;

import com.salatin.account.model.dto.request.RegistrationRequestDto;
import com.salatin.account.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;


    @GetMapping("/res")
    public String resource() {
        return "Here your resource";
    }

    @GetMapping("/res2")
    public ResponseEntity<String> resource2() {
        return new ResponseEntity<>("your response", HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
        @Valid @RequestBody RegistrationRequestDto request) {
        userService.create(request);
    }
}
