package com.salatin.account.service;

import org.keycloak.representations.idm.UserRepresentation;

public interface UserService {

    UserRepresentation save(UserRepresentation userRepresentation);
    UserRepresentation findById(String id);

    UserRepresentation findByEmail(String email);

    UserRepresentation findByPhoneNumber(String phoneNumber);
}
