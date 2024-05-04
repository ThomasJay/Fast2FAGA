package com.thomasjayconsulting.fast2faga.model;

import lombok.Data;

@Data
public class AuthenticateUserRequest {
    private String email;
    private String code;
}
