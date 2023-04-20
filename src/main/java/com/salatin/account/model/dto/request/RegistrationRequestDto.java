package com.salatin.account.model.dto.request;

import com.salatin.account.validation.FieldsValueMatch;
import com.salatin.account.validation.ValidEmail;
import com.salatin.account.validation.ValidMobile;
import com.salatin.account.validation.ValidPassword;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldsValueMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords do not match"
)
public class RegistrationRequestDto {
    @ValidEmail
    private String email;
    @NotNull(message = "First name can't be null")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "First name doesn't match")
    private String firstName;
    @NotNull(message = "Last name can't be null")
    @Pattern(regexp = "^[a-zA-Z]{2,50}$", message = "Last name doesn't match")
    private String lastName;
    @ValidPassword
    private String password;
    private String repeatPassword;
    @ValidMobile
    private String phoneNumber;
}
