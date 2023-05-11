package com.intership.app.habittracker.controller;

import com.intership.app.habittracker.entity.User;
import com.intership.app.habittracker.service.AuthService;
import com.intership.app.habittracker.service.CustomUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("user")
public class UserController {

    private final CustomUserService customUserService;

    private final AuthService authService;

    public UserController(CustomUserService customUserService, AuthService authService) {
        this.customUserService = customUserService;
        this.authService = authService;
    }

    @PostMapping("/activate/changePassword")
    public User activateChangePassword(@RequestHeader(name="Authorization") String authorizationHeader){
        User user=customUserService.getFromAuthentication(authorizationHeader);
        return customUserService.activeChangePassword(user);
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<?> activeChangePassword(@PathVariable String code){
        User user=customUserService.getByActivationCode(code);
        return authService.authenticate(user);
    }

    @PostMapping("/changePassword")
    public User changePassword(@RequestBody String newPassword,@RequestHeader(name="Authorization") String authorizationHeader){
        User user=customUserService.getFromAuthentication(authorizationHeader);
        return customUserService.changePassword(newPassword,user);
    }

    @PutMapping("/update")
    public User update(@RequestParam(value = "file",required = false) MultipartFile file,
                       @RequestParam(value = "text",required = false) String username,
                       @RequestHeader(name="Authorization") String authorizationHeader) throws IOException {
        User user=customUserService.getFromAuthentication(authorizationHeader);
        return customUserService.update(file,username,user);
    }
}
