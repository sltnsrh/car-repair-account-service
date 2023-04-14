package com.salatin.account.service.impl;

import com.salatin.account.service.AuthService;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AuthAdminService implements AuthService {
    private static final String GRANT_TYPE_PASSWORD = "password";

    private final WebClient webClient = WebClient.create();

    @Value("${admin.username}")
    private String ADMIN_USERNAME;
    @Value("${admin.password}")
    private String ADMIN_PASSWORD;
    @Value("${admin.client.id}")
    private String ADMIN_CLIENT_ID;

    @Override
    public String authAndGetJwt() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", GRANT_TYPE_PASSWORD);
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
}
