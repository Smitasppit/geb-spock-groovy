package au.com.securepay.test.automation;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

/**
 * Copyright(c) 2019 SecurePay Pty Ltd. All rights reserved by SecurePay Pty Ltd.
 * Loads request param values from test-data/*.data files
 */
public class TestDataManager {
    private static final Logger logger = LoggerFactory.getLogger(TestDataManager.class);

    private static TestDataManager testDataManager;
    private Configuration config = null;

    private TestDataManager() throws ConfigurationException, IOException {
        String testProfile = TestProfile.getInstance().getProfile();

        logger.info("Loading test data for profile '{}'", testProfile);
        config = loadTestData(testProfile);
    }

    private Configuration loadTestData(String testProfile) throws ConfigurationException {
        String testDataPath = "/test-data/" + testProfile + ".data";
        URL testDataURL = getClass().getResource(testDataPath);
        if (testDataURL != null) {
            logger.info("Loading test data from '{}'", testDataPath);

            String filePath = testDataURL.getFile();
            Parameters params = new Parameters();
            FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                            .configure(params.properties()
                                    .setFileName(filePath)
                                    .setIOFactory(new WhitespaceIOFactory()));
            return builder.getConfiguration();
        } else {
            logger.warn("Test data file '{}' cannot be found. Loading test data from a base profile.", testDataPath);
            int idx = testProfile.lastIndexOf('-');
            if (idx > 0) {
                return loadTestData(testProfile.substring(0, idx));
            } else {
                throw new RuntimeException("Cannot load test data.");
            }
        }
    }

    public static TestDataManager getInstance() throws ConfigurationException, IOException {
        if (testDataManager == null) {
            synchronized (TestDataManager.class) {
                if (testDataManager == null) {
                    testDataManager = new TestDataManager();
                }
            }
        }
        return testDataManager;
    }

    /**
     * Retrieve the value for a test data element given key based on the active profile.
     * @param key
     * @return Value
     */
    public String getData(String key) {
        return config.getString(key);
    }

    public class WhitespacePropertiesReader extends PropertiesConfiguration.PropertiesReader {

        /**
         * Constructor.
         *
         * @param reader A Reader.
         */
        public WhitespacePropertiesReader(Reader reader) {
            super(reader);
        }

        @Override
        protected void parseProperty(String line) {
            // simply split the line at the first '=' character
            int pos = line.indexOf('=');
            String key = line.substring(0, pos).trim();
            String value = line.substring(pos + 1).trim();
            // now store the key and the value of the property
            initPropertyName(key);
            initPropertyValue(value);
        }
    }

    public class WhitespaceIOFactory extends PropertiesConfiguration.DefaultIOFactory {
        @Override
        public PropertiesConfiguration.PropertiesReader createPropertiesReader(Reader in) {
            return new WhitespacePropertiesReader(in);
        }
    }
}
