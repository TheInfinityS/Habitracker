package com.intership.app.habittracker.security.oidc;

import com.intership.app.habittracker.entity.User;
import com.intership.app.habittracker.exception.OidcAuthenticationProcessingException;
import com.intership.app.habittracker.repository.UserRepository;
import com.intership.app.habittracker.security.UserPrincipal;
import com.intership.app.habittracker.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CustimOIdcUserService extends OidcUserService {
    public CustimOIdcUserService(){
        super();
    }

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser= super.loadUser(userRequest);
        try {
            return processOAuth2User(userRequest, oidcUser);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }

    }

    private OidcUser processOAuth2User(OidcUserRequest userRequest, OidcUser oidcUser) {
        OidcUserInfo oidcUserInfo = OidcUserInfoFactory.getOidcUserInfo(userRequest.getClientRegistration().getRegistrationId(), oidcUser.getAttributes());
        if(StringUtils.isEmpty(oidcUserInfo.getPreferredUsername())) {
            throw new OidcAuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional=userRepository.findByEmail(oidcUser.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            user = updateExistingUser(user, oidcUserInfo);
        } else {
            user = registerNewUser(userRequest, oidcUserInfo);
        }

        return UserPrincipal.create(user, oidcUser.getAttributes());
    }

    private User registerNewUser(OidcUserRequest userRequest, OidcUserInfo oidcUserInfo) {
        User user = new User();

        user.setProviderId(oidcUserInfo.getSubject());
        user.setUsername(oidcUserInfo.getPreferredUsername());
        user.setEmail(oidcUserInfo.getEmail());
        user.setIcon(oidcUserInfo.getPicture());
        user.setEmailVerified(true);
        user.setLocale(oidcUserInfo.getLocale());
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OidcUserInfo oidcUserInfo) {
        return userRepository.save(existingUser);
    }
}
