package org.maven.tutorial.examples.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("application")
public class ApplicationProperties {

    private String platform;

    public String getPlatform() {
        return platform;
    }

    public ApplicationProperties setPlatform(String platform) {
        this.platform = platform;
        return this;
    }
}
