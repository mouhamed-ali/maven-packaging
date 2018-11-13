package org.maven.tutorial.examples.controllers;

import org.maven.tutorial.examples.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PropertySource("classpath:common/description.properties")
public class ShowAppProprties {

    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private Environment env;


    @GetMapping
    public String showProperty() {

        return applicationProperties.getPlatform();
    }

    @GetMapping("/description")
    public String showDescription() {

        return env.getProperty("description");
    }
}
