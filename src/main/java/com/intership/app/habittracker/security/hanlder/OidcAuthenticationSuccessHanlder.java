package com.intership.app.habittracker.security.hanlder;

import com.intership.app.habittracker.security.jwt.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OidcAuthenticationSuccessHanlder extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    public OidcAuthenticationSuccessHanlder(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String jwt= jwtService.generateToken(authentication);
        System.out.println(jwt);
        response.setHeader("Authorization", "Bearer " + jwt);
        getRedirectStrategy().sendRedirect(request, response, "/");
    }
}
