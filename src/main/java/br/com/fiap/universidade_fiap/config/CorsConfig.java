package br.com.fiap.universidade_fiap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        // Em dev: Expo (19006) e Vite (5173). Adicione sua origem de produção quando publicar.
        cfg.setAllowedOrigins(List.of(
                "http://localhost:19006",
                "http://localhost:5173"
        ));
        // Se você preferir curinga por ambiente, use setAllowedOriginPatterns(List.of("*"))

        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization","Content-Type","Accept"));
        cfg.setAllowCredentials(false); // true só se realmente precisar de cookies
        cfg.setMaxAge(3600L); // cache do preflight

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", cfg); // só para a API
        return source;
    }
}
