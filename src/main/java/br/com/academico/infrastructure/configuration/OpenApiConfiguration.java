package br.com.academico.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI academicoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema Acadêmico API")
                        .description("API REST do MVP — matrícula, catálogo acadêmico e turmas")
                        .version("1.0.0"));
    }
}
