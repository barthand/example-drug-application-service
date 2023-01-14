package com.barthand.example.drugapplication.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("openfda")
@Data
public class OpenFDAProperties {

    private String url;
    
    private boolean enableCache;
    
    private String cronExpressionForCacheEviction;
    
}
