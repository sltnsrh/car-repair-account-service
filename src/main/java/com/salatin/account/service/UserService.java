package com.salatin.account.service;

import org.keycloak.representations.idm.UserRepresentation;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<UserRepresentation> save(UserRepresentation userRepresentation);
    Mono<UserRepresentation> findById(String id);

    Mono<UserRepresentation> findByEmail(String email);

    Mono<UserRepresentation> findByPhoneNumber(String phoneNumber);

    Mono<Void> addRole(String userId, String role);
}
