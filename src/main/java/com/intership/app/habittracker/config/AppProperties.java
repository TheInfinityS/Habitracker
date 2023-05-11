package com.intership.app.habittracker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth=new Auth();

    public static class Auth{
        private String tokenSecret;
        private long tokenExpirationMsec;

        private long refreshTokenExpirationMsec;

        public String getTokenSecret() {
            return tokenSecret;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }

        public long getTokenExpirationMsec() {
            return tokenExpirationMsec;
        }

        public void setTokenExpirationMsec(long tokenExpirationMsec) {
            this.tokenExpirationMsec = tokenExpirationMsec;
        }

        public long getRefreshTokenExpirationMsec() {
            return refreshTokenExpirationMsec;
        }

        public void setRefreshTokenExpirationMsec(long refreshTokenExpirationMsec) {
            this.refreshTokenExpirationMsec = refreshTokenExpirationMsec;
        }
    }

    public Auth getAuth() {
        return auth;
    }
}
