package au.com.securepay.test.automation.steps;

import au.com.securepay.test.automation.TestContext;
import au.com.securepay.test.automation.TestDataManager;
import au.com.securepay.test.automation.TestProfile;
import au.com.securepay.test.automation.request.api.PayloadBuilder;

/**
 * Copyright(c) 2020 SecurePay Pty Ltd. All rights reserved by SecurePay Pty Ltd.
 * Abstract super class extended by all step implementation classes. Holds a reference to
 * relevant {@link TestContext} and provides a bunch of helper methods
 */
public abstract class AbstractSteps {
    private TestContext testContext;

    public AbstractSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    public TestContext getTestContext() {
        return this.testContext;
    }

    protected PayloadBuilder getPayloadBuilder() throws Exception {
        return PayloadBuilder.getInstance();
    }

    protected TestProfile getTestProfile() throws Exception {
        return TestProfile.getInstance();
    }

    protected TestDataManager getTestDataManager() throws Exception {
        return TestDataManager.getInstance();
    }

}
