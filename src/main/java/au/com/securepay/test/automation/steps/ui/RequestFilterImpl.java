package au.com.securepay.test.automation.steps.ui;

import au.com.securepay.test.automation.TestContext;
import au.com.securepay.test.automation.UIRest;
import com.browserup.bup.filters.RequestFilter;
import com.browserup.bup.util.HttpMessageContents;
import com.browserup.bup.util.HttpMessageInfo;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestFilterImpl implements RequestFilter {
    TestContext testContext;
    private static final Logger logger = LoggerFactory.getLogger(RequestFilterImpl.class);

    public RequestFilterImpl(TestContext testContext) {
        this.testContext = testContext;
    }

    @Override
    public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
        UIRest uiRest = new UIRest();
        getTestContext().setCurrentUIRest(uiRest);

        logger.info(">>>>>>>>>>Request");
        logger.info(request.toString());
        logger.info(">>>>>>>>>>Request Contents");
        logger.info(contents.getTextContents());

        getTestContext().getCurrentUIRest().setRequest(request);
        getTestContext().getCurrentUIRest().setRequestContents(contents.getTextContents());

        return null;
    }

    public TestContext getTestContext() {
        return testContext;
    }

    public void setTestContext(TestContext testContext) {
        this.testContext = testContext;
    }
}
