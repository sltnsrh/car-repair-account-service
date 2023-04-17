package com.salatin.account.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${keycloak.host.url}")
    private String KEYCLOAK_HOST;
    @Value("${realm.name}")
    private String REALM_NAME;
    @Value("${admin.username}")
    private String ADMIN_USERNAME;
    @Value("${admin.password}")
    private String ADMIN_PASSWORD;
    @Value("${admin.client.id}")
    private String ADMIN_CLIENT_ID;

    @Bean
    public Keycloak getAdminKeycloakUser() {
        return KeycloakBuilder.builder()
            .serverUrl(KEYCLOAK_HOST)
            .grantType(OAuth2Constants.PASSWORD)
            .realm(REALM_NAME).clientId(ADMIN_CLIENT_ID)
            .username(ADMIN_USERNAME).password(ADMIN_PASSWORD)
            .build();
    }

    @Bean
    public UsersResource usersResource() {
        return getAdminKeycloakUser().realm(REALM_NAME).users();
    }

    @Bean
    public RealmResource realmResource() {
        return getAdminKeycloakUser().realm(REALM_NAME);
    }
}
