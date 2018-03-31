package org.maven.tutorial.examples.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/properties")
@PropertySource("classpath:app-parameters.properties")
public class ShowAppPropeties {

    @Autowired
    private Environment env;//retrieve application.properties + app-parameters.properties

    @PostMapping
    public String showProperty(@RequestParam("property") String property){

        return env.getProperty(property);
    }
}
