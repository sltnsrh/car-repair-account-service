package com.salatin.account.service.mapper;

import com.salatin.account.model.dto.response.UserResponseDto;
import java.util.List;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles",
        expression = "java(getUserRoles(userRepresentation.getId(), usersResource))")
    UserResponseDto toDto(UserRepresentation userRepresentation, UsersResource usersResource);

    @Named("getUserRoles")
    default List<String> getUserRoles(String id, UsersResource usersResource) {
        return usersResource.get(id).roles().realmLevel().listEffective().stream()
            .map(RoleRepresentation::getName)
            .toList();
    }
}
