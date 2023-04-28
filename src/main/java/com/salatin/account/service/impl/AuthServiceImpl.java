package com.salatin.account.service.impl;

import com.salatin.account.exception.MobileNumberAlreadyExistsException;
import com.salatin.account.exception.EmailAlreadyExistsException;
import com.salatin.account.model.dto.request.RegistrationRequestDto;
import com.salatin.account.service.AuthService;
import com.salatin.account.service.UserService;
import com.salatin.account.service.mapper.UserRepresentationMapper;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final UserRepresentationMapper userRepresentationMapper;


    @Override
    public Mono<UserRepresentation> register(RegistrationRequestDto requestDto) {
        var phoneNumber = requestDto.getPhoneNumber();

        if (mobileAlreadyExists(phoneNumber))
            return Mono.error(new MobileNumberAlreadyExistsException(
            "Phone number is already exists: " + phoneNumber));

        var email = requestDto.getEmail();

        if(emailAlreadyExists(email))
            return Mono.error(new EmailAlreadyExistsException(
                "Email is already exists: " + email));

        var user = userRepresentationMapper.toUserRepresentation(requestDto);

        return userService.save(user);
    }

    private boolean mobileAlreadyExists(String mobile) {

        return userService.findByPhoneNumber(mobile) != null;
    }

    private boolean emailAlreadyExists(String email) {

        return userService.findByEmail(email) != null;
    }
}
