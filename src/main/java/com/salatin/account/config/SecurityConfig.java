package com.salatin.account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests().requestMatchers("/register").permitAll()
            .and()
            .securityMatcher("/register")
            .authorizeHttpRequests()
            .anyRequest().hasRole("CUSTOMER")
            .and()
            .oauth2ResourceServer()
            .jwt();

        return http.build();
    }
}
