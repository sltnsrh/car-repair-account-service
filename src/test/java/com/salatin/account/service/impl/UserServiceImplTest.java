//package com.salatin.account.service.impl;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import com.salatin.account.exception.MobileNumberAlreadyExistsException;
//import com.salatin.account.exception.EmailAlreadyExistsException;
//import com.salatin.account.model.dto.request.RegistrationRequestDto;
//import com.salatin.account.service.mapper.UserRepresentationMapper;
//import java.util.List;
//import javax.ws.rs.core.Response;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.keycloak.admin.client.resource.UsersResource;
//import org.keycloak.representations.idm.UserRepresentation;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//@RequiredArgsConstructor
//class UserServiceImplTest {
//    private static final String EMAIL_TEST = "test@email.com";
//    private static final String MOBILE_TEST = "+380999998877";
//
//    @InjectMocks
//    private UserServiceImpl userService;
//    @Mock
//    private UsersResource usersResource;
//    @Mock
//    private UserRepresentationMapper mapper;
//
//    @Test
//    void createWhenMobileExistsThenTrowException() {
//        when(usersResource.searchByAttributes(MOBILE_TEST))
//                .thenReturn(List.of(new UserRepresentation()));
//
//        assertThrows(MobileNumberAlreadyExistsException.class,
//            () -> userService.create(createRegistrationDto()));
//    }
//
//    @Test
//    void createWhenEmailExistsThenTrowException() {
////        when(usersResource.searchByAttributes("phoneNumber:" + MOBILE_TEST))
////                .thenReturn(Collections.emptyList());
//        when(userService.findByPhoneNumber(MOBILE_TEST))
//            .thenReturn(null);
//
//        UserRepresentation user = new UserRepresentation();
//        user.setEmail(EMAIL_TEST);
//
//        var registrationDto = createRegistrationDto();
//
//        when(mapper.toUserRepresentation(registrationDto))
//                .thenReturn(user);
//
//        Response response = mock(Response.class);
//
//        when(response.getStatus()).thenReturn(409);
//
//        when(usersResource.create(user)).thenReturn(response);
//
//        assertThrows(EmailAlreadyExistsException.class,
//            () -> userService.create(registrationDto));
//    }
//
//    private RegistrationRequestDto createRegistrationDto() {
//        RegistrationRequestDto requestDto = new RegistrationRequestDto();
//        requestDto.setEmail(EMAIL_TEST);
//        requestDto.setMobile(MOBILE_TEST);
//
//        return requestDto;
//    }
//}