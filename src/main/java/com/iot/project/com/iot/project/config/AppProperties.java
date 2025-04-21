package com.iot.project.com.iot.project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter @Setter
@ConfigurationProperties(prefix = "app.config")
public class AppProperties {

    private String name;
    private String version;
    private String environment; 
    private String responseKey;
}
