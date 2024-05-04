package com.thomasjayconsulting.fast2faga.model;

import lombok.Data;

@Data
public class User {
    private String id;
    private String name;
    private String email;
    private String secret;
}
