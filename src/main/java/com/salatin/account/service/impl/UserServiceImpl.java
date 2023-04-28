package com.salatin.account.service.impl;

import com.salatin.account.service.UserService;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final static String PHONE_ATTRIBUTE = "phoneNumber:";

    private final UsersResource usersResource;

    @Override
    public Mono<UserRepresentation> save(UserRepresentation userRepresentation) {

        try (Response ignored = usersResource.create(userRepresentation)) {

            return Mono.just(usersResource.search(userRepresentation.getEmail()).get(0)).log();
        }
    }

    @Override
    public Mono<UserRepresentation> findById(String id) {
        log.info(() -> "Looking for a user with id: " + id);

        return Mono.fromCallable(() -> usersResource.get(id).toRepresentation())
            .onErrorResume(NotFoundException.class, ex ->
                Mono.error(new ResponseStatusException(HttpStatus.NO_CONTENT,
                    "Can't find a user with id: " + id))).log();
    }

    @Override
    public Mono<UserRepresentation> findByEmail(String email) {
        log.info(() -> "Looking for a user with email: " + email);
        var userRepresentations = usersResource.search(email);

        return userRepresentations.isEmpty() ? null : Mono.just(userRepresentations.get(0)).log();
    }

    @Override
    public Mono<UserRepresentation> findByPhoneNumber(String mobile) {
        log.info(() -> "Looking for a user with mobile: " + mobile);
        var userRepresentations = usersResource
            .searchByAttributes(PHONE_ATTRIBUTE + mobile);

        return userRepresentations.isEmpty() ? null : Mono.just(userRepresentations.get(0)).log();
    }
}
