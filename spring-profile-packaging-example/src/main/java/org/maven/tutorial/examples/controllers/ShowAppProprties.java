package org.maven.tutorial.examples.controllers;

import org.maven.tutorial.examples.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShowAppProprties {

    @Autowired
    ApplicationProperties applicationProperties;

    @GetMapping
    public String showProperty() {

        return applicationProperties.getPlatform();
    }
}
