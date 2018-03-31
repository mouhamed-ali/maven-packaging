package org.maven.tutorial.examples.sql;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;

public class TestDataBaseConnectivity {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataBaseConnectivity.class);
    private static final String SQL_EXT = ".sql";
    String ENCODING = "UTF-8";


    public String getCurrentScript() throws IOException {

        //the script must have the same name as the current class
        String scriptName = this.getClass().getSimpleName().concat(SQL_EXT);
        InputStream sqlStream = getClass().getResourceAsStream(scriptName);
        return IOUtils.toString(sqlStream, ENCODING).replaceAll("\r", "");
    }

    public String execute() {

        String sqlScript = null;
        try {

            sqlScript = getCurrentScript();
            LOGGER.info("sql script to be executed is : {}",sqlScript);
            return "OK";
        } catch (IOException e) {
            LOGGER.error("error occured while trying to execute the sql script.",e);
            return "KO";
        }
    }
}
