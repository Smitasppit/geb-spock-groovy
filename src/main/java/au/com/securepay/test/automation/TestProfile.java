package au.com.securepay.test.automation;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Copyright(c) 2019 SecurePay Pty Ltd. All rights reserved by SecurePay Pty Ltd.
 * Determines the test profile and loads properties for the relevant environment
 */
public class TestProfile {
    private static final Logger logger = LoggerFactory.getLogger(TestProfile.class);

    private static TestProfile testProfile = null;

    private String profile = null;
    private Properties properties = new Properties();

    private TestProfile() throws IOException {
        profile = StringUtils.trimToNull(System.getProperty("test-profile"));
        if (profile == null) {
            throw new RuntimeException("'test-profile' property is not defined.");
        }

        logger.info("Test profile: '{}'", profile);

        loadProfile(profile, properties);
        printTestProperties(properties);

        generateEnvironmentFile();
    }

    private void loadProfile(String profile, Properties properties) throws IOException {
        // Load base profiles first.
        int idx = profile.lastIndexOf('-');
        if (idx > 0) {
            loadProfile(profile.substring(0, idx), properties);
        }

        String testPropertyPath = "/environment/" + profile + ".properties";
        URL testPropertyURL = getClass().getResource(testPropertyPath);
        if (testPropertyURL != null) {
            logger.info("Loading test properties from {}", testPropertyPath);
            try (InputStream inputStream = testPropertyURL.openStream()) {
                properties.load(inputStream);
            }
        } else {
            logger.debug("Property file '{}' cannot be found. Skipped.", testPropertyPath);
        }
    }

    private void generateEnvironmentFile() throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("<environment>");
        Enumeration<String> propertyNames = (Enumeration<String>) properties.propertyNames();
        String browserType = System.getProperty("automation.browser");
        if ( browserType != null) {
            builder.append("<parameter><key>").append("Browser Type").append("</key><value>").append(browserType).append("</value></parameter>");
        }
        while (propertyNames.hasMoreElements()) {
            String key = propertyNames.nextElement();
            String value = properties.getProperty(key);
            builder.append("<parameter><key>").append(key).append("</key><value>").append(value).append("</value></parameter>");
        }
        builder.append("</environment>");
        String fileContents = builder.toString();
        FileUtils.write(new File("build/allure-results/environment.xml"), fileContents, StandardCharsets.UTF_8);
    }

    public static TestProfile getInstance() throws IOException {
        if (testProfile == null) {
            synchronized (TestProfile.class) {
                if (testProfile == null) {
                    testProfile = new TestProfile();
                }
            }
        }
        return  testProfile;
    }

    public String getProfile() {
        return profile;
    }

    /**
     * Retrieve the value for the given property key based on the active profile.
     * @param key
     * @return Value
     */
    public String get(String key) {
        return properties.getProperty(key);
    }

    private void printTestProperties(Properties properties) throws IOException {
        StringWriter writer = new StringWriter();
        properties.store(writer, "Test properties");
        logger.info(writer.getBuffer().toString());
    }
}
