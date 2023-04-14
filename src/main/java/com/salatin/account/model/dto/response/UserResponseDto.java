package com.salatin.account.model.dto.response;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class UserResponseDto {
    private String id;
    private Long createdTimestamp;
    private String email;
    private String username;
    private Boolean enabled;
    private Boolean emailVerified;
    private String firstName;
    private String lastName;
    private Map<String, List<String>> attributes;
    private Map<String, Boolean> access;
}
