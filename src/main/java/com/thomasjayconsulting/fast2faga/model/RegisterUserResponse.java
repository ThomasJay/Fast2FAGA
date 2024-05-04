package com.thomasjayconsulting.fast2faga.model;

import lombok.Data;

@Data
public class RegisterUserResponse {
    private String email;
    private String qrCode;
}
