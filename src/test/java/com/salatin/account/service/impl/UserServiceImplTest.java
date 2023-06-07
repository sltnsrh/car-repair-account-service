package com.salatin.account.service.impl;

import static org.mockito.Mockito.mock;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private final static String EMAIL = "test@email.com";
    private final static String ID = "user-id";
    private final static String PHONE_NUMBER = "+380999998877";
    private final static String PHONE_ATTRIBUTE = "phoneNumber:";
    private final static SimpleGrantedAuthority ADMIN_AUTHORITY =
            new SimpleGrantedAuthority("ROLE_admin");

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UsersResource usersResource;
    @Mock
    private JwtAuthenticationToken authenticationToken;
    private UserRepresentation userRepresentation;

    @BeforeEach
    void init() {
        userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(EMAIL);
        userRepresentation.setId(ID);
    }

    @Test
    void saveWhenValidDataThenReturnUserRepresentation() {
        Mockito.when(usersResource.search(EMAIL))
            .thenReturn(Collections.singletonList(userRepresentation));

        StepVerifier.create(userService.save(userRepresentation))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void findByIdWhenValidIdAndAdminRequestsThenReturnUserRepresentation() {
        UserResource userResource = mock(UserResource.class);

        Mockito.when(usersResource.get(ID)).thenReturn(userResource);
        Mockito.when(userResource.toRepresentation()).thenReturn(userRepresentation);
        Mockito.when(authenticationToken.getAuthorities())
                .thenReturn(Collections.singletonList(ADMIN_AUTHORITY));

        StepVerifier.create(userService.findInfoById(ID, authenticationToken))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void findByEmailWhenUserExistsThenReturnUserRepresentation() {
        Mockito.when(usersResource.search(EMAIL))
            .thenReturn(Collections.singletonList(userRepresentation));

        StepVerifier.create(userService.findByEmail(EMAIL))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void findByPhoneNumberWhenUserExistsThenReturnUserRepresentation() {
        Mockito.when(usersResource.searchByAttributes(PHONE_ATTRIBUTE + PHONE_NUMBER))
            .thenReturn(Collections.singletonList(userRepresentation));

        StepVerifier.create(userService.findByPhoneNumber(PHONE_NUMBER))
            .expectNextCount(1)
            .verifyComplete();
    }
}
