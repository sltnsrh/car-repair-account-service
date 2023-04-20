package com.salatin.account.service.mapper;

import com.salatin.account.model.dto.request.RegistrationRequestDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

@Component
public class UserRepresentationMapper {
    private static final String MOBILE_ATTRIBUTE = "phoneNumber";

    public UserRepresentation toUserRepresentation(RegistrationRequestDto userDto) {
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
        attributes.put(MOBILE_ATTRIBUTE, Collections.singletonList(userDto.getPhoneNumber()));

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
