package com.capstone.warehousesvc.config;

import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
public class SpringDocConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Warehouse Management System API")
                        .version("1.0.0")
                        .description("API for warehouse management operations"));
    }
    
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/api/**")
                .packagesToScan("com.capstone.warehousesvc.controllers")
                .build();
    }
    
    @Bean
    @Primary
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        // Create a new clean ObjectMapper dedicated to OpenAPI schema generation
        ObjectMapper docMapper = new ObjectMapper();
        
        // Configure serialization options
        docMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        docMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        docMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        docMapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, true);
        
        // Configure deserialization options
        docMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        // Register SimpleModule to handle DTO circular references
        SimpleModule module = new SimpleModule("OpenAPI");
        docMapper.registerModule(module);
        
        // Essential for handling infinite recursion in models
        docMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
        
        return new ModelResolver(docMapper);
    }
} 