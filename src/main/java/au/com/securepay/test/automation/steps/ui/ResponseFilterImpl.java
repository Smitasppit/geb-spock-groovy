package au.com.securepay.test.automation.steps.ui;

import au.com.securepay.test.automation.TestContext;
import com.browserup.bup.filters.ResponseFilter;
import com.browserup.bup.util.HttpMessageContents;
import com.browserup.bup.util.HttpMessageInfo;


import io.netty.handler.codec.http.HttpResponse;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;

public class ResponseFilterImpl implements ResponseFilter {
    TestContext testContext;
    private static final Logger logger = LoggerFactory.getLogger(ResponseFilterImpl.class);

    public ResponseFilterImpl(TestContext testContext) {
        this.testContext = testContext;
    }

    @Override
    public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {
        logger.info("<<<<<<<<<<Response");
        logger.info(response.toString());
        logger.info("<<<<<<<<<<Response Contents");
        if (APPLICATION_JSON.equalsIgnoreCase(response.headers().get(HttpHeaders.CONTENT_TYPE))) {
            logger.info(contents.getTextContents());
        }else{
            logger.info("Skipped for non json response");
        }

        getTestContext().getCurrentUIRest().setResponse(response);
        getTestContext().getCurrentUIRest().setResponseContents(contents.getTextContents());
        getTestContext().addUIRest(getTestContext().getCurrentUIRest());

    }

    public TestContext getTestContext() {
        return testContext;
    }

    public void setTestContext(TestContext testContext) {
        this.testContext = testContext;
    }
}
