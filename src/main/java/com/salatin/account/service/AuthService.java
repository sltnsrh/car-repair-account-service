package com.salatin.account.service;

import com.salatin.account.model.dto.request.RegistrationRequestDto;
import org.keycloak.representations.idm.UserRepresentation;

public interface AuthService {

    UserRepresentation register(RegistrationRequestDto requestDto);
}
