package com.intership.app.habittracker.config;

import com.intership.app.habittracker.security.CustomUserDetailsService;
import com.intership.app.habittracker.security.RestAccessDeniedHandler;
import com.intership.app.habittracker.security.RestAuthenticationEntryPoint;
import com.intership.app.habittracker.security.hanlder.OidcAuthenticationSuccessHanlder;
import com.intership.app.habittracker.security.jwt.JwtAuthentiocationFilter;
import com.intership.app.habittracker.security.oidc.CustimOIdcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig {

    @Autowired
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Autowired
    private JwtAuthentiocationFilter jwtAuthentiocationFilter;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustimOIdcUserService custimOIdcUserService;

    @Autowired
    private OidcAuthenticationSuccessHanlder oidcAuthenticationSuccessHanlder;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(restAuthenticationEntryPoint);
                    httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(restAccessDeniedHandler);
                })
                .authorizeRequests(request->{
                    request.requestMatchers("/","/auth/**", "/user/activate/**","/js/**", "/error**").permitAll();
                    request.anyRequest().authenticated();
                })
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> {
                    httpSecurityOAuth2LoginConfigurer.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.oidcUserService(custimOIdcUserService));
                    httpSecurityOAuth2LoginConfigurer.successHandler(oidcAuthenticationSuccessHanlder);
                })
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.logoutSuccessUrl("/"))
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthentiocationFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
