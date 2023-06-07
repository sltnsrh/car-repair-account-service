package com.salatin.account.service.impl;

import com.salatin.account.service.UserService;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    public Mono<UserRepresentation> findInfoById(String userId,
                                                 JwtAuthenticationToken jwtAuthenticationToken) {
        return checkUserAccess(userId, jwtAuthenticationToken)
                .then(this.findById(userId));
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
    public Mono<Void> addRoleByUserId(@NotNull String userId, String role) {
        var userResource = usersResource.get(userId);

        try {
            if (userHasRole(userResource, role)) {
                return Mono.error(new ResponseStatusException(HttpStatus.ACCEPTED,
                        "User already has role " + role));
            }
        } catch (NotFoundException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Can't find the user with id: " + userId));
        }

        try {
            var realmRoleToAdd = realmResource.roles().get(role).toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(realmRoleToAdd));
            log.info("Role {} was added to the user with id {}", role, userId);
            return Mono.empty();
        } catch (NotFoundException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Can't find the role: " + role));
        }
    }

    @Override
    public Mono<Void> deleteRoleByUserId(String userId, String role) {
        var userResource = usersResource.get(userId);

        try {
            if (!userHasRole(userResource, role)) {
                return Mono.error(new ResponseStatusException(HttpStatus.ACCEPTED,
                        "User hasn't such a role: " + role));
            }
        } catch (NotFoundException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Can't find the user with id: " + userId));
        }

        var realmRoleToDelete = realmResource.roles().get(role).toRepresentation();
        userResource.roles().realmLevel().remove(Collections.singletonList(realmRoleToDelete));
        log.info("Role {} was deleted from the user with id {}", role, userId);
        return Mono.empty();
    }

    private Mono<Void> checkUserAccess(String requestedUserId, JwtAuthenticationToken authenticationToken) {

        if (isAdminOrManager(authenticationToken)) {
            return Mono.empty();
        } else if (isCustomerOrMechanic(authenticationToken)
                && !authenticationToken.getName().equals(requestedUserId)) {

            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Access denied. You can only retrieve your own information"));
        }
        return Mono.empty();
    }

    private Mono<UserRepresentation> findById(String id) {
        log.info(() -> "Looking for a user with id: " + id);

        return Mono.fromCallable(() -> usersResource.get(id).toRepresentation())
                .onErrorResume(NotFoundException.class, ex ->
                        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Can't find the user with id: " + id))).log();
    }

    private boolean isAdminOrManager(JwtAuthenticationToken authenticationToken) {
        var adminAuthority = new SimpleGrantedAuthority("ROLE_admin");
        var mechanicAuthority = new SimpleGrantedAuthority("ROLE_manager");

        return authenticationToken.getAuthorities().contains(adminAuthority)
                || authenticationToken.getAuthorities().contains(mechanicAuthority);
    }

    private boolean isCustomerOrMechanic(JwtAuthenticationToken authenticationToken) {
        var customerAuthority = new SimpleGrantedAuthority("ROLE_customer");
        var mechanicAuthority = new SimpleGrantedAuthority("ROLE_mechanic");

        return authenticationToken.getAuthorities().contains(customerAuthority)
                || authenticationToken.getAuthorities().contains(mechanicAuthority);
    }

    private boolean userHasRole(UserResource userResource, String role) {
        return userResource.roles().realmLevel().listAll().stream()
                .anyMatch(r -> r.getName().equals(role));
    }
}
