package com.example.bankcards.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.security")
@Getter @Setter
public class SecurityProperties {
    private List<String> allowedSources;
    private List<String> allowedMethods;
    private List<String> allowedHeaders;
    private List<String> ignoredUrls;
}