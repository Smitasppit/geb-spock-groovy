package au.com.securepay.test.automation.steps.ui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Level;

import au.com.securepay.test.automation.TestContext;
import au.com.securepay.test.automation.steps.AbstractSteps;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.Architecture;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

/**
 * Copyright(c) 2020 SecurePay Pty Ltd. All rights reserved by SecurePay Pty Ltd.
 * Abstract super class extended by all UI step implementation classes.
 */
public abstract class UISteps extends AbstractSteps {
    private static String WEBDRIVER_CACHE_PATH = "C:\\AutomationTest\\Tools\\webdriver";
    private static Logger logger = LoggerFactory.getLogger(UISteps.class);
    static {

        initDriver();

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide() {
            @Override
            public void afterEvent(final LogEvent event) {
                if (!event.getStatus().equals(LogEvent.EventStatus.FAIL)) {
                    Allure.getLifecycle().getCurrentTestCaseOrStep().ifPresent(parentUuid -> {
                        getScreenshotAsBytes()
                                .ifPresent(bytes -> Allure.getLifecycle().addAttachment("Screenshot", "image/png", "png", bytes));
                    });
                }
                super.afterEvent(event);
            }

            private Optional<byte[]> getScreenshotAsBytes() {
                try {
                    return WebDriverRunner.hasWebDriverStarted()
                            ? Optional.of(((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES))
                            : Optional.empty();
                } catch (WebDriverException e) {
                    return Optional.empty();
                }
            }
        });
    }

    private static void configDriver(Architecture arch){
        Path cachePath = Paths.get(WEBDRIVER_CACHE_PATH);

        if (!Files.exists(cachePath)) {
            try {
                Files.createDirectories(cachePath);
                WebDriverManager.globalConfig().setCachePath(WEBDRIVER_CACHE_PATH)
                        .setResolutionCachePath(WEBDRIVER_CACHE_PATH)
                        .setArchitecture(arch);
            } catch (IOException e) {
                // if can't create path, best try with default value.
                logger.error(e.getMessage(), e);
            }
        } else {
            WebDriverManager.globalConfig().setCachePath(WEBDRIVER_CACHE_PATH)
                    .setResolutionCachePath(WEBDRIVER_CACHE_PATH)
                    .setArchitecture(arch);
        }
    }

    private static void initDriver(){
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String browserType = "CHROME";
        if (System.getProperty("automation.browser") != null){
            browserType = System.getProperty("automation.browser");
        }
        switch (browserType){
            case "CHROME":
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-extensions");
                options.addArguments("--no-sandbox");
                options.addArguments("--window-size=1920,1200");
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                Configuration.browserCapabilities = capabilities;
                Configuration.browser = "chrome";
                configDriver(Architecture.X64);
                break;
            case "FIREFOX":
                Configuration.browser = "firefox";
                configDriver(Architecture.X64);
                break;
            case "EDGE":
                Configuration.browser = "edge";
                configDriver(Architecture.X64);
                break;
            case "IE":
                capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                Configuration.browserCapabilities = capabilities;
                Configuration.browser = "ie";
                // IE is very slow with 64 driver. Special handling for IE by applying 32 driver.
                configDriver(Architecture.X32);
                break;
            default:
                configDriver(Architecture.X64);
                break;
        }

        // Leave it to downstream test project to control headless with selenide.headless system prop
        if (System.getProperty("selenide.headless")==null) {
            Configuration.headless = true;
        }
    }

    public UISteps(TestContext testContext) {
        super(testContext);
    }

    /**
     * Load the page at the url defined against the relevant property key in active test profile.
     * @param propertyKey
     * @throws Exception
     */
    protected void openUrl(String propertyKey) throws Exception {
        open(getTestProfile().get(propertyKey));
    }

    /**
     * Resolve the value from test-data based on the given key (and active test-profile) and enter the value to a
     * text-field which is determined based on the given element id.
     * @param elementId
     * @param dataKey
     * @throws Exception
     */
    protected void enterDataByIdAndDataKey(String elementId, String dataKey) throws Exception {
        enterDataByIdAndValue(elementId, getTestDataManager().getData(dataKey));
    }

    /**
     * Enter the given value to a text-field which is determined based on the given element id.
     * @param elementId
     * @throws Exception
     */
    protected void enterDataByIdAndValue(String elementId, String value) throws Exception {
        $(By.name(elementId)).setValue(value);
    }
}
