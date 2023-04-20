package com.salatin.account.service.impl;

import com.salatin.account.service.UserService;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersResource usersResource;

    @Override
    public UserRepresentation save(UserRepresentation userRepresentation) {

        try (Response ignored = usersResource.create(userRepresentation)) {

            return usersResource.search(userRepresentation.getEmail()).get(0);
        }
    }

    @Override
    public UserRepresentation findById(String id) {
        return usersResource.get(id).toRepresentation();
    }

    @Override
    public UserRepresentation findByEmail(String email) {
        var userRepresentations = usersResource.search(email);

        return userRepresentations.isEmpty() ? null : userRepresentations.get(0);
    }

    @Override
    public UserRepresentation findByPhoneNumber(String mobile) {
        var userRepresentations = usersResource
            .searchByAttributes("phoneNumber:" + mobile);

        return userRepresentations.isEmpty() ? null : userRepresentations.get(0);
    }
}
