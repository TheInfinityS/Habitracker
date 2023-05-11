package com.intership.app.habittracker.web.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("user_id")
    private Long id;

    @JsonProperty("user_name")
    private String name;

    @JsonProperty("user_email")
    private String email;
}
