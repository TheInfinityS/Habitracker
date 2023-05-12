package com.intership.app.habittracker.service;

import com.intership.app.habittracker.entity.EmailDetails;
import com.intership.app.habittracker.entity.User;
import com.intership.app.habittracker.exception.BadRequestException;
import com.intership.app.habittracker.security.UserPrincipal;
import com.intership.app.habittracker.security.jwt.JwtService;
import com.intership.app.habittracker.web.dto.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<?> login(JwtRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        return getTokenResponse(authentication);
    }


    public ResponseEntity<?>  getTokenResponse(Authentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtService.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.ok(new AuthenticationResponse(token,
                userPrincipal.getId(),
                userPrincipal.getName(),
                userPrincipal.getEmail()));
    }

    public ResponseEntity<?>  register(SignUpRequest signUpRequest) {
        if(customUserService.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setEmailVerified(false);
        user.setChangablePassword(false);

        User result = customUserService.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/")
                .buildAndExpand(result.getId()).toUri();

        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to HabitTracker. Please, visit next link: http://localhost:8080/auth/activate/%s",
                user.getUsername(),
                user.getActivationCode()
        );
        EmailDetails emailDetails=new EmailDetails();
        emailDetails.setRecipient(result.getEmail());
        emailDetails.setSubject("Activation code");
        emailDetails.setMsgBody(message);

        emailService.sendSimpleMail(emailDetails);

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully@"));
    }

    public String encryptPassword(String newPassword) {
        return passwordEncoder.encode(newPassword);
    }
}
