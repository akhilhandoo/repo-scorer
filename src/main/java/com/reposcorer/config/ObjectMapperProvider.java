package com.reposcorer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class ObjectMapperProvider {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
