package com.salatin.account.service.impl;

import com.salatin.account.exception.ExceptionResponse;
import com.salatin.account.exception.UserAlreadyExistsException;
import com.salatin.account.model.dto.request.RegistrationRequestDto;
import com.salatin.account.service.UserService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.ErrorResponse;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String MOBILE_ATTRIBUTE = "mobile";
    private static final String CREATE_USER_URI = "/admin/realms/%s/users";

    private final WebClient webClient = WebClient.create();

    @Value("${admin.username}")
    private String ADMIN_USERNAME;
    @Value("${admin.password}")
    private String ADMIN_PASSWORD;
    @Value("${admin.client.id}")
    private String ADMIN_CLIENT_ID;
    @Value("${keycloak.host}")
    private String KEYCLOAK_HOST;
    @Value("${realm.name}")
    private String REALM_NAME;

    @Override
    public void create(RegistrationRequestDto userDto) {
        String token = authAdmin();
        UserRepresentation user = createUserRepresentation(userDto);

        Mono<HttpStatus> response = webClient.post()
            .uri(String.format(KEYCLOAK_HOST + CREATE_USER_URI, REALM_NAME))
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

        HttpStatus status = response.block();

        assert status != null;
        if (status.value() != HttpStatus.CREATED.value()) {
            throw new RuntimeException("Can't register a new user. Response status is: "
                + status.value());
        }
    }

    private String authAdmin() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("username", ADMIN_USERNAME);
        formData.add("password", ADMIN_PASSWORD);
        formData.add("client_id", ADMIN_CLIENT_ID);

        return Objects.requireNonNull(webClient.post()
                .uri("http://localhost:8083/realms/car-repair-realm/protocol/openid-connect/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(Map.class)
                .block())
            .get("access_token").toString();
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
