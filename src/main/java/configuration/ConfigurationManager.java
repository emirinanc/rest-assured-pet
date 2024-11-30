package configuration;


import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class ConfigurationManager {
    private static final Properties properties = new Properties();
    private static ConfigurationManager instance;

    private ConfigurationManager() {
        loadProperties("config.properties");
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    private void loadProperties(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                log.warn("Property file '{}' not found in classpath", fileName);
            }
        } catch (IOException e) {
            log.error("Error loading properties file: {}", fileName, e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
