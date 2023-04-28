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
        checkIfMobileAlreadyExists(requestDto.getPhoneNumber());
        checkIfEmailAlreadyExists(requestDto.getEmail());

        UserRepresentation user = userRepresentationMapper.toUserRepresentation(requestDto);

        return userService.save(user);
    }

    private void checkIfMobileAlreadyExists(String mobile) {

        if (userService.findByPhoneNumber(mobile) != null) {
            throw new MobileNumberAlreadyExistsException(
                "Phone number is already exists: " + mobile);
        }
    }

    private void checkIfEmailAlreadyExists(String email) {

        if (userService.findByEmail(email) != null) {
            throw new EmailAlreadyExistsException(
                "Can't register a new user. Email is already exists");
        }
    }
}
