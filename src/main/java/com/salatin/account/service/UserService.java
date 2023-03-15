package com.salatin.account.service;

import com.salatin.account.model.dto.request.RegistrationRequestDto;
import com.salatin.account.model.dto.request.UserKeycloakDto;

public interface UserService {
    void create(RegistrationRequestDto userDto);
}
