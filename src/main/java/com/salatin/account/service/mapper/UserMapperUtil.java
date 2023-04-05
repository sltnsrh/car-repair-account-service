package com.salatin.account.service.mapper;

import com.salatin.account.model.status.UserRole;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperUtil {
    public static final UserRole CUSTOMER_ROLE = UserRole.CUSTOMER;
    public static final String REDUNDANT_MOBILE_SIGNS = "[ ()\\-.]";
    public static final String EMPTY_SIGN = "";

    private final PasswordEncoder passwordEncoder;

    @Named("setEncodedPassword")
    String setEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Named("setCustomerRole")
    UserRole setCustomerRole(String value) {
        return CUSTOMER_ROLE;
    }

    @Named("setMobile")
    String setMobile(String mobile) {
        return mobile.replaceAll(REDUNDANT_MOBILE_SIGNS, EMPTY_SIGN);
    }
}
