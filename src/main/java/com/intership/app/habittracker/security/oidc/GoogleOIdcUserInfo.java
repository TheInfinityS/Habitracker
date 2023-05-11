package com.intership.app.habittracker.security.oidc;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;

public class GoogleOIdcUserInfo extends OidcUserInfo {

    public GoogleOIdcUserInfo(Map<String, Object> claims) {
        super(claims);
    }

    @Override
    public String getPreferredUsername() {
        return (String) this.getClaims().get("name");
    }

    @Override
    public String getSubject() {
        return (String) this.getClaims().get("sub");
    }


    @Override
    public String getPicture() {
        return (String) this.getClaims().get("picture");
    }

    @Override
    public String getEmail() {
        return (String) this.getClaims().get("email");
    }

    @Override
    public String getLocale() {
        return (String) this.getClaims().get("locale");
    }
}
