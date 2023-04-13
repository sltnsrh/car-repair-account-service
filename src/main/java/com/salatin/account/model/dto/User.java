package com.salatin.account.model.dto;

import java.util.Map;
import lombok.Data;

@Data
public class User {
    private String id;
    private Long createdTimestamp;
    private String email;
    private String username;
    private Boolean enabled;
    private Boolean emailVerified;
    private String firstName;
    private String lastName;
    private Map<String, String> attributes;
    private Map<String, Boolean> access;
}
