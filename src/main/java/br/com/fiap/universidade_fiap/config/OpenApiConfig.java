package br.com.fiap.universidade_fiap.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiDocs() {
        return new OpenAPI()
                .info(new Info()
                        .title("Universidade FIAP API")
                        .description("Endpoints REST para integração com o app mobile")
                        .version("v1"))
                .externalDocs(new ExternalDocumentation()
                        .description("README / Documentação do projeto")
                        .url("https://seu-link"));
    }
}
