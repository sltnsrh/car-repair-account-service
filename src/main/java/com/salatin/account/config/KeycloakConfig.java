//package com.salatin.account.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class KeycloakConfig {
//
//    @Bean
//    public Keycloak keycloak() {
//        return KeycloakBuilder.builder()
//            .serverUrl("http://localhost:8083")
//            .realm("car-repair-realm")
//            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
//            .clientId("admin-cli2")
//            .clientSecret("BN0mIQ8VN91hAR9a2niUhvSWogEP34yX")
////            .username("carrepairshopua@gmail.com")
////            .password("4dm1npaSSwor@")
////            .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build())
//            .build();
//    }
//
//    @Bean
//    public UsersResource userResource() {
//        return keycloak().realm("car-repair-realm").users();
//    }
//}
