package com.salatin.account.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
            .serverUrl("http://localhost:8083/auth")
            .realm("car-repair-realm")
            .clientId("admin-cli")
            .username("kc_admin")
            .password("kc_password")
            .build();
    }

    @Bean
    public UsersResource userResource() {
        return keycloak().realm("car-repair-realm").users();
    }
}
