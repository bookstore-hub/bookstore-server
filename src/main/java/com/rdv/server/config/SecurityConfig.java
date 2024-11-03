package com.rdv.server.config;

import com.rdv.server.authentication.token.JwtTokenAuthorizationOncePerRequestFilter;
import com.rdv.server.authentication.token.JwtUnAuthorizedResponseAuthenticationEntryPoint;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    private final JwtUnAuthorizedResponseAuthenticationEntryPoint jwtUnAuthorizedResponseAuthenticationEntryPoint;
    private final UserDetailsService jwtUserDetailsService;
    private final JwtTokenAuthorizationOncePerRequestFilter jwtAuthenticationTokenFilter;
    private final String firebaseConfig;

    public SecurityConfig(JwtUnAuthorizedResponseAuthenticationEntryPoint jwtUnAuthorizedResponseAuthenticationEntryPoint,
                          UserDetailsService jwtUserDetailsService, JwtTokenAuthorizationOncePerRequestFilter jwtAuthenticationTokenFilter,
                          @Value("${app.firebase-config}") String firebaseConfig) {
        this.jwtUnAuthorizedResponseAuthenticationEntryPoint = jwtUnAuthorizedResponseAuthenticationEntryPoint;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.firebaseConfig = firebaseConfig;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(jwtUserDetailsService)
            .passwordEncoder(passwordEncoderBean());
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoderBean());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfig).getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "RDV");
        return FirebaseMessaging.getInstance(app);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtUnAuthorizedResponseAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .requestMatchers(
                        "/authenticate",
                        "/resources/**",
                        "/user/registration",
                        "/user/resendRegistrationToken/**",
                        "/user/resetPassword",
                        "/user/saveNewPassword",
                        "/images/upload",
                        "/sendTestNotificationWithData",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/resources/**",
                        "/v3/api-docs/**",
                        "/images/**",
                        "/css/**",
                        "/user/registrationConfirm/**",
                        "/user/enterNewPassword/**",
                        "/retrieveTextsForLanguage/**",
                        "/checkEmailTaken/**",
                        "/checkUsernameTaken/**",
                        "/test/ticketmaster",
                        "/"
                ).permitAll();
        httpSecurity
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity
                .headers()
                .frameOptions().sameOrigin()
                .cacheControl(); //disable caching
        return httpSecurity.build();
    }

}