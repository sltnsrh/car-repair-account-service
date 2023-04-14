package com.salatin.account.service.mapper;

import com.salatin.account.model.dto.response.UserResponseDto;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toDto(UserRepresentation userRepresentation);
}
