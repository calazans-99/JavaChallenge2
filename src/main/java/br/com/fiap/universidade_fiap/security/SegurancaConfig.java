package br.com.fiap.universidade_fiap.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // se quiser usar @PreAuthorize depois
public class SegurancaConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        // públicos
                        .requestMatchers("/", "/login", "/do-login",
                                "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()

                        // (DEV) liberar H2 console — REMOVA em prod
                        .requestMatchers("/h2-console/**").permitAll()

                        // apenas ADMIN
                        .requestMatchers("/usuarios/**", "/funcao/**").hasRole("ADMIN")

                        // ADMIN ou OPERADOR
                        .requestMatchers("/motos/**", "/sensores/**", "/patios/**", "/status-moto/**")
                        .hasAnyRole("ADMIN", "OPERADOR")

                        // demais precisam estar logados
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login").permitAll()
                        .loginProcessingUrl("/do-login")   // <-- bate com o action do seu form
                        .defaultSuccessUrl("/index", true)
                        .failureUrl("/login?falha=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/acesso_negado")
                )

                // (DEV) H2 console funciona com frames + sem CSRF — REMOVA em prod
                .headers(h -> h.frameOptions(frame -> frame.sameOrigin()))
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // força padrão; verifica qualquer hash $2a$... inclusive custo 12
    }
}