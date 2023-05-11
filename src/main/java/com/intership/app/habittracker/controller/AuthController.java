package com.intership.app.habittracker.controller;

import com.intership.app.habittracker.service.AuthService;
import com.intership.app.habittracker.service.CustomUserService;
import com.intership.app.habittracker.web.dto.auth.JwtRequest;
import com.intership.app.habittracker.web.dto.auth.SignUpRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final CustomUserService customUserService;

    public AuthController(AuthService authService, CustomUserService customUserService) {
        this.authService = authService;
        this.customUserService = customUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<?>  login(@Valid @RequestBody JwtRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?>  register(@Valid @RequestBody SignUpRequest signUpRequest){
        return authService.register(signUpRequest);
    }

    @PostMapping("/activate/{code}")
    public String activate(@PathVariable String code) {
        boolean isActivated = customUserService.activateUser(code);
        String message=new String();

        if (isActivated) {
            message="User successfully activated";
        } else {
            message= "Activation code is not found!";
        }

        return message;
    }

}
