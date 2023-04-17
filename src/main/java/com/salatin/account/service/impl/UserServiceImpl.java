package com.salatin.account.service.impl;

import com.salatin.account.exception.UserAlreadyExistsException;
import com.salatin.account.model.dto.request.RegistrationRequestDto;
import com.salatin.account.service.UserService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String MOBILE_ATTRIBUTE = "mobile";

    private final UsersResource usersResource;

    @Override
    public UserRepresentation create(RegistrationRequestDto userDto) {
        UserRepresentation user = createUserRepresentation(userDto);

        try (Response createUserResponse = usersResource.create(user)) {

            if (createUserResponse != null
                && createUserResponse.getStatus() == HttpStatus.CONFLICT.value()) {
                throw new UserAlreadyExistsException(
                    "Can't register a new user. Email is already exists");
            }
        }

        return findByEmail(userDto.getEmail());
    }

    @Override
    public UserRepresentation findByEmail(String email) {
        return usersResource.search(email).get(0);
    }


    @Override
    public UserRepresentation findByPhoneNumber(String mobile) {
        return null;
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
