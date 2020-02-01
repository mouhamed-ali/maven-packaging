package org.maven.tutorial.examples.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/properties")
public class DisplayProperties {

    @Autowired
    private Environment env;

    @GetMapping({"", "/"})
    public ResponseEntity<String> showProperty(@RequestParam(value = "property", required = false) String property) {

        if (!StringUtils.hasLength(property)) {

            return new ResponseEntity<>("Oops, you can't use this service until you provide a property name", HttpStatus.OK);
        }

        String prop = env.getProperty(property);
        if (StringUtils.hasLength(prop)) {

            return new ResponseEntity<>(String.format("%s -> %s", property, prop), HttpStatus.OK);
        } else {

            return new ResponseEntity<>("Cannot found this property", HttpStatus.NOT_FOUND);
        }
    }
}
