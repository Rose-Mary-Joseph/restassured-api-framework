package com.api.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ConfigReader {

    private static ConfigReader instance;
    private final Properties properties;

    private ConfigReader() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(
                "src/test/resources/config/config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage());
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String get(String key) {
        String systemProp = System.getProperty(key);
        return systemProp != null ? systemProp : properties.getProperty(key);
    }

    public String getBaseUrl()         { return get("base.url"); }
    public int getConnectionTimeout()  { return Integer.parseInt(get("connection.timeout")); }
    public int getResponseTimeout()    { return Integer.parseInt(get("response.timeout")); }
    public boolean shouldLogAll()      { return Boolean.parseBoolean(get("log.all")); }
    public String getApiKey()          { return get("api.key"); }
}