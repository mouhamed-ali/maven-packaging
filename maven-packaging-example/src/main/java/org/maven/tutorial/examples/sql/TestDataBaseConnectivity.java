package org.maven.tutorial.examples.sql;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class TestDataBaseConnectivity {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataBaseConnectivity.class);
    private static final String SCRIPT_NAME = "select.sql";
    private static final String ENCODING = "UTF-8";


    private String loadFromCurrentPackage() throws IOException {

        LOGGER.debug("Start loading the script file from the current package");
        InputStream sqlStream = getClass().getResourceAsStream(SCRIPT_NAME);
        return IOUtils.toString(sqlStream, ENCODING).replaceAll("\r", "");
    }

    private String loadFromResources() throws IOException {

        try {

            File scriptFile = new ClassPathResource(SCRIPT_NAME).getFile();
            return new String(Files.readAllBytes(scriptFile.toPath()));
        } catch (Exception ex) {

            LOGGER.warn("Could not load the script file from resources.", ex);
            return loadFromCurrentPackage();
        }
    }

    public String execute() {

        String sqlScript = null;
        try {

            sqlScript = loadFromResources();
            LOGGER.info("sql script to be executed is : {}", sqlScript);
            return sqlScript;
        } catch (IOException e) {
            LOGGER.error("error occurred while trying to execute the sql script.", e);
            return "KO";
        }
    }
}
