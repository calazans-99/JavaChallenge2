package com.grupo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("dev")
public class DevSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // libera o H2 Console (GET e POST)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()   // ou .anyRequest().permitAll() se quiser tudo aberto em dev
                )
                // o H2 Console usa formulário simples sem CSRF-token
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                // o console usa <iframe>; precisa permitir
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}
