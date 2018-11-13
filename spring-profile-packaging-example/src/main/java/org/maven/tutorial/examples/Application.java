package org.maven.tutorial.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.util.StringUtils;

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
                        "spring.config.location:" + applicationFiles)
                .build()
                .run(args);
    }

    private static String getCommonFilesDir() {

        System.out.println("Enter the common directory full path : ");
        String result = reader.next();
        return transform(result, "common");
    }

    private static String transform(String result, String dirName) {

        if (!StringUtils.hasLength(result))
            return null;
        if (!result.endsWith("/"))
            result += "/";
        if (!result.endsWith(dirName + "/"))
            result += dirName + "/";

        File dir = Paths.get(result).toFile();
        if (!dir.exists() || !dir.isDirectory())
            throw new RuntimeException(String.format("%s does not exist", result));

        return "classpath:" + result + ",file:" + result;
    }

    private static String getConfigFilesDir() {

        System.out.println("Enter the config-files directory full path : ");
        String result = reader.next();
        return transform(result, "config-files");
    }
}
