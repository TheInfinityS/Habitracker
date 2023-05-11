package com.intership.app.habittracker.web.dto.auth;

import lombok.Data;

@Data
public class JwtRequest {

    private String email;

    private String password;
}
