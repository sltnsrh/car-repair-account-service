package com.salatin.account.model.dto.request;

import lombok.Data;

@Data
public class UserKeycloakDto {
    private String userName;
    private String emailId;
    private String password;
    private String firstname;
    private String lastName;
}
