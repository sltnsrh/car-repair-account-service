package com.salatin.account.service.impl;

import com.salatin.account.exception.MobileNumberAlreadyExistsException;
import com.salatin.account.exception.UserAlreadyExistsException;
import com.salatin.account.model.dto.request.RegistrationRequestDto;
import com.salatin.account.service.UserService;
import com.salatin.account.service.mapper.UserRepresentationMapper;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersResource usersResource;
    private final UserRepresentationMapper userRepresentationMapper;

    @Override
    public UserRepresentation create(RegistrationRequestDto userDto) {
        checkIfMobileAlreadyExists(userDto.getMobile());

        UserRepresentation user = userRepresentationMapper.toUserRepresentation(userDto);

        checkEmailAndSaveUser(user);

        return findByEmail(userDto.getEmail());
    }

    @Override
    public UserRepresentation findById(String id) {
        return usersResource.get(id).toRepresentation();
    }

    @Override
    public UserRepresentation findByEmail(String email) {
        return usersResource.search(email).get(0);
    }

    @Override
    public UserRepresentation findByPhoneNumber(String mobile) {
        var userRepresentations = usersResource
            .searchByAttributes("phoneNumber:" + mobile);

        return userRepresentations.isEmpty() ? null : userRepresentations.get(0);
    }

    private void checkIfMobileAlreadyExists(String mobile) {
        if (findByPhoneNumber(mobile) != null) {
            throw new MobileNumberAlreadyExistsException(
                "Mobile number is already exists: " + mobile);
        }
    }

    private void checkEmailAndSaveUser(UserRepresentation user) {
        try (Response createUserResponse = usersResource.create(user)) {

            if (createUserResponse != null
                && createUserResponse.getStatus() == HttpStatus.CONFLICT.value()) {
                throw new UserAlreadyExistsException(
                    "Can't register a new user. Email is already exists");
            }
        }
    }
}
