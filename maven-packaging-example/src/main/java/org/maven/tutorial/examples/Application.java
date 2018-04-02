package org.maven.tutorial.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);
        /*
            Spring Boot provides the class ApplicationPidFileWriter, which will then write the PID into a file.
            You can activate it by adding it as a listener to the SpringApplication.
            The constructor of ApplicationPidFileWriter can also take a String or a File object with a custom
            filename. Then you can read the PID from that file and use it in your scripts.
            source : https://stackoverflow.com/questions/40864111/how-to-get-process-id-of-a-spring-boot-application
         */
    }
}
