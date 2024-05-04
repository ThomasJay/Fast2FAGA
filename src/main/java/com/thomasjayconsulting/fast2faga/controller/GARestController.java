package com.thomasjayconsulting.fast2faga.controller;

import com.thomasjayconsulting.fast2faga.model.AuthenticateUserRequest;
import com.thomasjayconsulting.fast2faga.model.RegisterUserRequest;
import com.thomasjayconsulting.fast2faga.model.RegisterUserResponse;
import com.thomasjayconsulting.fast2faga.model.User;
import com.thomasjayconsulting.fast2faga.service.GAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class GARestController {


    private GAService gaService;

    public GARestController(GAService gaService) {
        this.gaService = gaService;
    }

    @PostMapping("/api/v1/register")
    public RegisterUserResponse register(@RequestBody RegisterUserRequest registerUserRequest) {

        String qrCodeURL = gaService.registerNewUser(registerUserRequest.getName(), registerUserRequest.getEmail());

        RegisterUserResponse registerUserResponse = new RegisterUserResponse();

        registerUserResponse.setEmail(registerUserRequest.getEmail());

        registerUserResponse.setQrCode(qrCodeURL);

        return registerUserResponse;
    }

    @PostMapping("/api/v1/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticateUserRequest authenticateUserRequest) {

        User user = gaService.authenticate(authenticateUserRequest.getEmail(), authenticateUserRequest.getCode());

        if (user != null) {
            user.setSecret(null);
            return ResponseEntity.ok(user);
        }

        return new ResponseEntity("{\"msg\":\"Invalid email or code\"}", HttpStatus.UNAUTHORIZED);

    }




}
