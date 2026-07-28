package br.com.academico.infrastructure.persistence.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "br.com.academico.infrastructure.persistence.repository")
@EntityScan(basePackages = "br.com.academico.domain.model")
public class PersistenceConfiguration {
}
