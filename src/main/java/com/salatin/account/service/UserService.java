package com.salatin.account.service;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<UserRepresentation> save(UserRepresentation userRepresentation);

    Mono<UserRepresentation> findInfoById(String userId,
                                          JwtAuthenticationToken jwtAuthenticationToken);

    Mono<UserRepresentation> findByEmail(String email);

    Mono<UserRepresentation> findByPhoneNumber(String phoneNumber);

    Mono<Void> addRoleByUserId(String userId, String role);

    Mono<Void> deleteRoleByUserId(String userId, String role);
}
