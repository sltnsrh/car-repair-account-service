package com.salatin.account.service.impl;

import com.salatin.account.service.UserService;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.resource.RealmResource;
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
    private final RealmResource realmResource;

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
                Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Can't find the user with id: " + id))).log();
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

    @Override
    public Mono<Void> addRole(@NotNull String userId, String role) {
        var userResource = usersResource.get(userId);

        try {
            var userAlreadyHasRole = userResource.roles().realmLevel().listAll().stream()
                    .anyMatch(r -> r.getName().equals(role));

            if (userAlreadyHasRole) {
                return Mono.error(new ResponseStatusException(HttpStatus.ACCEPTED,
                        "User already has role " + role));
            }
        } catch (NotFoundException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Can't find the user with id: " + userId));
        }

        return Mono.defer(() -> {
            try {
                var realmRoleToAdd = realmResource.roles().get(role).toRepresentation();
                userResource.roles().realmLevel().add(Collections.singletonList(realmRoleToAdd));
                log.info("Role {} was added to the user with id {}", role, userId);
                return Mono.empty();
            } catch (NotFoundException e) {
                return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Can't find the role: " + role));
            }
        });
    }
}
