package com.example.cleaningservices.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Configuração de CORS — permite que o frontend acesse a API
// Sem isso o navegador bloqueia a requisição por segurança
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")           // aplica para todos os endpoints
                .allowedOrigins("*")         // permite qualquer origem (frontend local ou Vercel)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}

