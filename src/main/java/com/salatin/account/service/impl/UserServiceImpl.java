package com.salatin.account.service.impl;

import com.salatin.account.exception.UserAlreadyExistsException;
import com.salatin.account.model.dto.User;
import com.salatin.account.model.dto.request.RegistrationRequestDto;
import com.salatin.account.service.UserService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String MOBILE_ATTRIBUTE = "mobile";
    private static final String CREATE_USER_URI = "/admin/realms/{realm}/users";

    private final WebClient webClient = WebClient.create();
    private final AuthAdminService authAdminService;

    @Value("${keycloak.host.url}")
    private String KEYCLOAK_HOST;
    @Value("${realm.name}")
    private String REALM_NAME;

    @Override
    public Mono<User> create(RegistrationRequestDto userDto) {

        String token = authAdminService.authAndGetJwt();
        HttpStatus status = registerOnAuthServer(userDto, token);

        assert status != null;
        if (status.value() != HttpStatus.CREATED.value()) {
            throw new RuntimeException("Can't register a new user. Response status is: "
                + status.value());
        }

        return findByEmail(userDto.getEmail());
    }

    @Override
    public Mono<User> findByEmail(String email) {
        String token = authAdminService.authAndGetJwt();

        return webClient.post()
            .uri(uriBuilder -> uriBuilder.host(KEYCLOAK_HOST)
                .path("/admin/realms/car-repair-realm/users")
                .queryParam("username", "some@email.com")
                .build())
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<User>>() {})
            .map(users -> users.get(0));
    }

    @Override
    public User findByPhoneNumber(String mobile) {
        return null;
    }

    private HttpStatus registerOnAuthServer(RegistrationRequestDto userDto, String token) {

        UserRepresentation user = createUserRepresentation(userDto);

        Mono<HttpStatus> response = webClient.post()
            .uri(KEYCLOAK_HOST + CREATE_USER_URI, REALM_NAME)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(user)
            .headers(headers -> {
                headers.setBearerAuth(token);
            })
            .retrieve()
            .onStatus(HttpStatus.CONFLICT::equals, clientResponse
                -> Mono.error(new UserAlreadyExistsException(
                String.format("User %s is already registered", userDto.getEmail())
            )))
            .toBodilessEntity()
            .map(responseEntity -> (HttpStatus) responseEntity.getStatusCode());

       return response.block();
    }



    private UserRepresentation createUserRepresentation(RegistrationRequestDto userDto) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.getEmail());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEnabled(true);

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(userDto.getPassword());
        credentials.setTemporary(false);
        user.setCredentials(Collections.singletonList(credentials));

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put(MOBILE_ATTRIBUTE, Collections.singletonList(userDto.getMobile()));
        user.setAttributes(attributes);
        user.setCredentials(createCredentials(userDto.getPassword()));

        return user;
    }

    private List<CredentialRepresentation> createCredentials(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);

        List<CredentialRepresentation> credentials = new ArrayList<>();
        credentials.add(credentialRepresentation);
        return credentials;
    }
}
