package com.salatin.account.service;

import com.salatin.account.model.dto.request.RegistrationRequestDto;
import org.keycloak.representations.idm.UserRepresentation;

public interface UserService {
    UserRepresentation create(RegistrationRequestDto userDto);

    UserRepresentation findByEmail(String email);

    UserRepresentation findByPhoneNumber(String phoneNumber);
}
