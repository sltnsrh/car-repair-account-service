package com.salatin.account.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private final static String EMAIL = "test@email.com";
    private final static String ID = "user-id";
    private final static String PHONE_NUMBER = "+380999998877";
    private final static String PHONE_ATTRIBUTE = "phoneNumber:";

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UsersResource usersResource;

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

        assertNotNull(userService.save(userRepresentation));
    }

    @Test
    void findByEmailWhenUserExistsThenReturnUserRepresentation() {
        Mockito.when(usersResource.search(EMAIL))
            .thenReturn(Collections.singletonList(userRepresentation));

        assertNotNull(userService.findByEmail(EMAIL));
    }

    @Test
    void findByPhoneNumberWhenUserExistsThenReturnUserRepresentation() {
        Mockito.when(usersResource.searchByAttributes(PHONE_ATTRIBUTE + PHONE_NUMBER))
            .thenReturn(Collections.singletonList(userRepresentation));

        assertNotNull(userService.findByPhoneNumber(PHONE_NUMBER));
    }
}
