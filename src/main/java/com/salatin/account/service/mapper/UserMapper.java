package com.salatin.account.service.mapper;

import com.salatin.account.model.dto.User;
import com.salatin.account.model.dto.request.RegistrationRequestDto;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapperUtil.class})
@RequiredArgsConstructor
public abstract class UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "role", target = "role", qualifiedByName = "setCustomerRole")
    @Mapping(source = "mobile", target = "mobile", qualifiedByName = "setMobile")
    public abstract User toCustomerUser(RegistrationRequestDto request);
}
