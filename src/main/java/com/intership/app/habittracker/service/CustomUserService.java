package com.intership.app.habittracker.service;

import com.intership.app.habittracker.entity.EmailDetails;
import com.intership.app.habittracker.entity.User;
import com.intership.app.habittracker.resository.UserRepository;
import com.intership.app.habittracker.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class CustomUserService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    private final JwtService jwtService;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public CustomUserService(UserRepository userRepository, EmailService emailService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
    }

    public User getByActivationCode(String code){
        return userRepository.findByActivationCode(code);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean activateUser(String code) {
        User user = getByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setEmailVerified(true);
        save(user);

        return true;
    }

    public User activeChangePassword(User user) {
        if(user.getEmailVerified()){
            user.setActivationCode(UUID.randomUUID().toString());
            User result = save(user);

            String message = String.format(
                    "Hello, %s! \n" +
                            "To change password visit next link: http://localhost:8080/user/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            EmailDetails emailDetails=new EmailDetails();
            emailDetails.setRecipient(result.getEmail());
            emailDetails.setSubject("Activation code");
            emailDetails.setMsgBody(message);

            emailService.sendSimpleMail(emailDetails);

            return result;
        }
        else return user;
    }

    public User getFromAuthentication(String authorizationHeader){
        String jwt = authorizationHeader.substring(7);
        String email=jwtService.extractEmail(jwt);
        return getByEmail(email);
    }

    public User changePassword(String newPassword, User user) {
        user.setPassword(newPassword);
        user.setChangablePassword(false);
        return userRepository.save(user);
    }

    public User update(MultipartFile file, String username, User user) throws IOException {
        System.out.println(username);
        if(file!=null){
            File uploadDir=new File(uploadPath);
            if(!uploadDir.exists())
                uploadDir.mkdir();
            String uuidFile = UUID.randomUUID().toString();
            String fileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath+"/"+fileName));
            user.setIcon(fileName);
        }
        if(username!=null){
            user.setUsername(username);
        }
        return userRepository.save(user);
    }
}
