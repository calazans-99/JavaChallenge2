package br.com.fiap.universidade_fiap.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // se quiser usar @PreAuthorize depois
public class SegurancaConfig {

    /**
     * Cadeia 1 — SOMENTE API (/api/**):
     * - Stateless
     * - httpBasic
     * - CSRF desabilitado
     * - CORS habilitado (usa bean de CorsConfig, se presente)
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Deixe público se quiser um ping de saúde
                        .requestMatchers("/api/v1/health", "/api/v1/public/**").permitAll()
                        // Ajuste se quiser granular por método:
                        .requestMatchers(HttpMethod.GET, "/api/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Cadeia 2 — Web MVC (tudo que NÃO é /api/**):
     * Mantém seu login de formulário, regras e exceções como estavam.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth
                        // públicos
                        .requestMatchers("/", "/login", "/do-login",
                                "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()

                        // (DEV) H2 console — REMOVA em prod
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
                        .loginProcessingUrl("/do-login")   // bate com o action do seu form
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

                // (DEV) H2 console funciona com frames + exceção de CSRF — REMOVA em prod
                .headers(h -> h.frameOptions(frame -> frame.sameOrigin()))
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // $2a$..., custo padrão
    }
}
