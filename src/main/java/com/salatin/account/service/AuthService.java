package com.salatin.account.service;

import com.salatin.account.model.dto.request.RegistrationRequestDto;
import org.keycloak.representations.idm.UserRepresentation;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<UserRepresentation> register(RegistrationRequestDto requestDto);
}
