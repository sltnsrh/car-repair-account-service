package com.salatin.account.service;

import com.salatin.account.model.dto.User;
import com.salatin.account.model.dto.request.RegistrationRequestDto;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> create(RegistrationRequestDto userDto);

    Mono<User> findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);
}
