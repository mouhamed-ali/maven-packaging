package org.maven.tutorial.examples.controllers;


import org.maven.tutorial.examples.sql.TestDataBaseConnectivity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sql/test")
public class TestSqlScript {

    @GetMapping(value = {"/",""})
    public String execute(){

        return new TestDataBaseConnectivity().execute();
    }
}
