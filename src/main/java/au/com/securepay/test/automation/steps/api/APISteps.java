package au.com.securepay.test.automation.steps.api;

import au.com.securepay.test.automation.TestContext;
import au.com.securepay.test.automation.steps.AbstractSteps;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Copyright(c) 2019 SecurePay Pty Ltd. All rights reserved by SecurePay Pty Ltd.
 * Abstract super class extended by all API step implementation classes.
 */
public abstract class APISteps extends AbstractSteps {

    public APISteps(TestContext testContext) {
        super(testContext);
        /*
         * Push the common param here for xml related API request templates *.hbs like below.
         * {{#if nt_platform}}
         * <NABTransactMessage>
         * {{else}}
         * <SecurePayMessage>
         * {{/if}}
         * Example can be found in 3ds test suit xmlapi-initauth-3ds.hbs
         */
        String platform = StringUtils.trimToNull(System.getProperty("automation.platform"));
        if ((platform != null) && platform.equalsIgnoreCase("NT")) {
            getTestContext().putRequestParam("nt_platform", "true");
        }
    }


    /**
     * Assert that the JSON or XML response element in the given path is equal to the given value.
     * @param path
     * @param value
     */
    protected void assertEqualsInBody(String path, Object value) {
        getTestContext().getResponse().assertThat().body(path, equalTo(value));
    }

    /**
     * Assert that the JSON element in the list is equal to the given value.
     * @param path
     * @param value
     * @param index
     */
    protected void assertEqualsInBody(String path, Object value, int index) {
        Assert.assertEquals(value,
                getTestContext().getResponse().extract().jsonPath().getList(path).get(index).toString());
    }

    /**
     * Assert that the JSON or XML response element in the given path is not null value.
     * @param path
     */
    protected void assertNotNullValue(String path) {
        getTestContext().getResponse().assertThat().body(path, notNullValue());
    }

    /**
     * Assert that the JSON or XML response element in the given path is not empty string.
     * @param path
     */
    protected void assertNotEmptyString(String path) {
        getTestContext().getResponse().assertThat().body(path, is(not(emptyString())));
    }

    /**
     * Returns a (rest-assured) RequestSpecification which is wrapped to include a AllureRestAssured filter. This
     * ensures that the requests and responses are logged in AllureReports.
     * @return
     */
    protected RequestSpecification givenWrappedApiSpec()  {
        return given().filter(new AllureRestAssured());
    }

    public RequestSpecification requestSpecification(String propertiesUrl, ContentType contentType) {
        return new RequestSpecBuilder().setBaseUri(propertiesUrl)
                .setContentType(contentType).build();
    }

    public ResponseSpecification responseSpecification() {
        return new ResponseSpecBuilder().expectStatusCode(200).build();
    }

    public ResponseSpecification responseSpecification(ContentType contentType) {
        return new ResponseSpecBuilder().expectStatusCode(200)
                .expectContentType(contentType).build();
    }
}
