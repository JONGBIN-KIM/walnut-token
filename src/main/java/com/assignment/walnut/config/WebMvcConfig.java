package com.assignment.walnut.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ISO8583MessageConverter iso8583MessageConverter;

    public WebMvcConfig(ISO8583MessageConverter iso8583MessageConverter) {
        this.iso8583MessageConverter = iso8583MessageConverter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(iso8583MessageConverter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/logs")
                .allowedOrigins("http://localhost:8443", "http://localhost:8444")
                .allowedMethods("GET", "POST");

    }
}
