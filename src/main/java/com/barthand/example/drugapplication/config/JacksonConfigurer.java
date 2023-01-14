package com.barthand.example.drugapplication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.cloud.openfeign.support.SortJacksonModule;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class JacksonConfigurer {

    public JacksonConfigurer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void addObjectMapperModules() {
        objectMapper.registerModule(new PageJacksonModule());
        objectMapper.registerModule(new SortJacksonModule());
    }
}
