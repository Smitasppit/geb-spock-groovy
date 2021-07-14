package au.com.securepay.test.automation.steps.ui;

import au.com.securepay.test.automation.TestContext;
import com.browserup.bup.filters.RequestFilter;
import com.browserup.bup.filters.ResponseFilter;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UIRestSteps extends UISteps {
    private static final Logger logger = LoggerFactory.getLogger(UIRestSteps.class);

    public UIRestSteps(TestContext testContext)  {
        super(testContext);
        Selenide.open();
        if (WebDriverRunner.getSelenideProxy() != null) {
            RequestFilter requestFilter = WebDriverRunner.getSelenideProxy().requestFilter("request");

            if (requestFilter == null) {
                WebDriverRunner.getSelenideProxy().addRequestFilter("request", new RequestFilterImpl(getTestContext()));
            } else {
                ((RequestFilterImpl) requestFilter).setTestContext(getTestContext());
            }

            ResponseFilter responseFilter = WebDriverRunner.getSelenideProxy().responseFilter("response");

            if (responseFilter == null) {
                WebDriverRunner.getSelenideProxy().addResponseFilter("response", new ResponseFilterImpl(getTestContext()));
            } else {
                ((ResponseFilterImpl) responseFilter).setTestContext(getTestContext());
            }
        }
    }

}
