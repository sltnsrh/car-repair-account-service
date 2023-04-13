package com.salatin.account.service;

import com.salatin.account.model.User;
import com.salatin.account.model.dto.request.RegistrationRequestDto;

public interface UserService {
    void create(RegistrationRequestDto userDto);

    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);
}
