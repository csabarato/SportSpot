package com.sportspot.sportspot.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

    private static InputStream inputStream;
    private static Properties properties;

    public static Properties getDevProperties() {

        try {
            properties = new Properties();
            String propFileName = "config-local.properties";

            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException(propFileName + "property file not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    return properties;
    }
}
