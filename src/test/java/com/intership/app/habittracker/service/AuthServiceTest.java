package com.intership.app.habittracker.service;

import com.intership.app.habittracker.security.jwt.JwtService;
import com.intership.app.habittracker.web.dto.auth.ApiResponse;
import com.intership.app.habittracker.web.dto.auth.JwtRequest;
import com.intership.app.habittracker.web.dto.auth.SignUpRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {


    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;


    @MockBean
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    @Autowired
    private CustomUserService customUserService;


    @Test
    void login() {
        JwtRequest loginRequest=new JwtRequest();
        loginRequest.setEmail("madaraitf@mail.ru");
        loginRequest.setPassword("12345");

        var response=authService.login(loginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void getTokenResponse() {
    }

    @Test
    void register() {
        SignUpRequest signUpRequest=new SignUpRequest("Max","asuraitf@mail.ru","123");

        var responseEntity=authService.register(signUpRequest);

        ApiResponse apiResponse=new ApiResponse(true,"User registered successfully@");

        assertEquals(responseEntity.getBody(),apiResponse);
    }

    @Test
    void encryptPassword() {
    }
}