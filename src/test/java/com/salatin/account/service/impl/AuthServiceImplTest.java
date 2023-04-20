package com.salatin.account.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.salatin.account.exception.EmailAlreadyExistsException;
import com.salatin.account.exception.MobileNumberAlreadyExistsException;
import com.salatin.account.model.dto.request.RegistrationRequestDto;
import com.salatin.account.service.UserService;
import com.salatin.account.service.mapper.UserRepresentationMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class AuthServiceImplTest {
    private static final String EMAIL_TEST = "test@email.com";
    private static final String MOBILE_TEST = "+380999998877";

    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private UserService userService;
    @Mock
    private UserRepresentationMapper mapper;

    @Test
    void registerWhenMobileExistsThenTrowException() {
        when(userService.findByPhoneNumber(MOBILE_TEST))
                .thenReturn(new UserRepresentation());

        assertThrows(MobileNumberAlreadyExistsException.class,
            () -> authService.register(createRegistrationDto()));
    }

    @Test
    void registerWhenEmailExistsThenTrowException() {
        when(userService.findByPhoneNumber(MOBILE_TEST)).thenReturn(null);
        when(userService.findByEmail(EMAIL_TEST)).thenReturn(new UserRepresentation());

        assertThrows(EmailAlreadyExistsException.class,
            () -> authService.register(createRegistrationDto()));
    }

    @Test
    void registerWhenValidDataThenReturnUserRepresentation() {
        var userRepresentation = new UserRepresentation();
        var registrationDto = createRegistrationDto();

        when(mapper.toUserRepresentation(registrationDto)).thenReturn(userRepresentation);
        when(userService.save(userRepresentation)).thenReturn(userRepresentation);
        when(userService.findByPhoneNumber(MOBILE_TEST)).thenReturn(null);
        when(userService.findByEmail(EMAIL_TEST)).thenReturn(null);

        assertNotNull(authService.register(registrationDto));
    }

    private RegistrationRequestDto createRegistrationDto() {
        RegistrationRequestDto requestDto = new RegistrationRequestDto();
        requestDto.setEmail(EMAIL_TEST);
        requestDto.setPhoneNumber(MOBILE_TEST);

        return requestDto;
    }
}
