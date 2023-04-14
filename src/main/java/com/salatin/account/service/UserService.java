package com.salatin.account.service;

import com.salatin.account.model.dto.response.UserResponseDto;
import com.salatin.account.model.dto.request.RegistrationRequestDto;
import org.keycloak.representations.idm.UserRepresentation;
import reactor.core.publisher.Mono;

public interface UserService {
    UserRepresentation create(RegistrationRequestDto userDto);

    UserRepresentation findByEmail(String email);

    UserResponseDto findByPhoneNumber(String phoneNumber);
}
