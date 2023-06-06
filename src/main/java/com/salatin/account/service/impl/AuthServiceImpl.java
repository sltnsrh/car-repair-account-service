package com.salatin.account.service.impl;

import com.salatin.account.model.dto.request.RegistrationRequestDto;
import com.salatin.account.service.AuthService;
import com.salatin.account.service.UserService;
import com.salatin.account.service.mapper.UserRepresentationMapper;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    public static final String CUSTOMER_ROLE = "customer";

    private final UserService userService;
    private final UserRepresentationMapper userRepresentationMapper;

    @Override
    public Mono<UserRepresentation> register(RegistrationRequestDto requestDto) {
        var phoneNumber = requestDto.getPhoneNumber();

        if (mobileAlreadyExists(phoneNumber))
            return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,
                    "Phone number is already exists: " + phoneNumber));

        var email = requestDto.getEmail();

        if(emailAlreadyExists(email))
            return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT,
                "Email is already exists: " + email));

        var userRepresentation = userRepresentationMapper.toUserRepresentation(requestDto);

        return userService.save(userRepresentation)
                .map(user -> {
                    userService.addRoleByUserId(user.getId(), CUSTOMER_ROLE);
                    return user;
                });
    }

    private boolean mobileAlreadyExists(String mobile) {
        return userService.findByPhoneNumber(mobile) != null;
    }

    private boolean emailAlreadyExists(String email) {
        return userService.findByEmail(email) != null;
    }
}
