package org.maven.tutorial.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.file.Paths;
import java.util.Scanner;

@SpringBootApplication
/*
 * check this for details and more examples
 * http://roufid.com/load-multiple-configuration-files-different-directories-spring-boot/
 */

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final Scanner reader = new Scanner(System.in);

    public static void main(String[] args) {

        //get the configuration files directories
        String configFilePath = getConfigFilesDir();
        String commonFiles = getCommonFilesDir();
        reader.close();

        String applicationFiles = "classpath:application-version.yml," + configFilePath + "," + commonFiles;
        LOGGER.info("spring.config.location : {}", applicationFiles);

        new SpringApplicationBuilder(Application.class)
                .properties(
                        "spring.config.name:application,description",
                        "spring.config.location:" + applicationFiles
                )
                .build()
                .run(args);
    }

    private static String getCommonFilesDir() {

        System.out.println("Enter the common directory full path : ");
        String result = reader.next();
        return transform(result);
    }

    private static String transform(String result) {

        Assert.hasLength(result, "The directory name must not be null or empty");

        if (!result.endsWith("/"))
            result += "/";

        File dir = Paths.get(result).toFile();
        if (!dir.exists() || !dir.isDirectory())
            throw new RuntimeException(String.format("%s directory does not exist", result));

        //return "classpath:" + result + ",file:" + result;
        return "file:" + result;
    }

    private static String getConfigFilesDir() {

        System.out.println("Enter the config directory full path : ");
        String result = reader.next();
        return transform(result);
    }
}
