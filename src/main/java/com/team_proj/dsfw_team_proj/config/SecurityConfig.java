package com.team_proj.dsfw_team_proj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        HttpSecurity httpSecurity = http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // allows static resources
                        .requestMatchers(
                                "/styles/**",
                                "/scripts/**",
                                "/images/**",
                                "/govuk-assets.assets/**"
                        ).permitAll()

                        // allows public pages
                        .requestMatchers(
                                "/",
                                "/home",
                                "/login",
                                "/register",
                                "/error"
                        ).permitAll()

                        // everything else locked e.g. admin pages
                        .anyRequest().authenticated()
                );

        // Allow frames for H2 Console (if you are using it)
                //.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}