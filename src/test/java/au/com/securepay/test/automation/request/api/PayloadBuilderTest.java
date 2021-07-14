package au.com.securepay.test.automation.request.api;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Copyright(c) 2020 SecurePay Pty Ltd. All rights reserved by SecurePay Pty Ltd.
 * Unit tests for {@link PayloadBuilder}
 */
public class PayloadBuilderTest {

    @Test
    public void testRenderJsonPayload() throws Exception {
        PayloadBuilder payloadBuilder = PayloadBuilder.getInstance();
        Map<String, Object> requestParams = getRequestParams();
        String payload = payloadBuilder.getPayload(requestParams, "sample-json-template");
        assertNotNull(payload);
        assertPayload(requestParams, payload);
    }

    private void assertPayload(Map<String, Object> requestParams, String payload) throws Exception {
        String expectedPayload =  "{"
                    + "\"amount\": " + requestParams.get("amount") + ","
                    + "\"currency\": \"" + requestParams.get("currency") + "\","
                    + "\"customerDetails\": {"
                        + "\"billingAddress\": {"
                            + "\"firstName\": \"" + requestParams.get("customerBillingFirstName") + "\","
                            + "\"lastName\": \"" + requestParams.get("customerBillingLastName") + "\","
                            + "\"address1\": \"" + requestParams.get("customerBillingAddress1") + "\","
                            + "\"address2\": \"" + requestParams.get("customerBillingAddress2") + "\","
                            + "\"city\": \"" + requestParams.get("customerBillingCity") + "\","
                            + "\"postcode\": \"" + requestParams.get("customerBillingPostcode") + "\","
                            + "\"state\": \"" + requestParams.get("customerBillingState") + "\""
                        + "},"
                        + "\"emailAddress\": \"" + requestParams.get("customerEmail") + "\""
                    + "},"
                    + "\"ip\": \"" + requestParams.get("sourceIP") + "\","
                    + "\"merchantId\": \"" + requestParams.get("merchantId") + "\","
                    + "\"merchantOrderReference\": \"" + requestParams.get("merchantOrderReference") + "\","
                    + "\"paymentType\": \"" + requestParams.get("paymentType") + "\","
                    + "\"threedSecureCheckDetails\": {"
                        + "\"source\": \"" + requestParams.get("threeDsSource") + "\""
                    + "}"
                + "}";
        ObjectMapper mapper = new ObjectMapper();
        assertEquals(mapper.readTree(expectedPayload), mapper.readTree(payload));
    }

    private Map<String, Object> getRequestParams() {
        Map<String, Object> requestParams = new HashMap<>();
        Faker faker = new Faker(new Locale("en-AU"));
        String amount = String.valueOf(faker.number().numberBetween(1, 10000));
        String customerFirstName = faker.address().firstName();
        String customerLastName = faker.address().lastName();
        String customerAddressLine1 = faker.address().streetAddress();
        String customerAddressLine2 = faker.address().secondaryAddress();
        String customerCity = faker.address().city();
        String customerPostcode = faker.address().zipCode();
        String state = faker.address().state();
        String emailAddress = faker.internet().safeEmailAddress();
        String ip = faker.internet().ipV4Address();
        String merchantId = faker.letterify("??????", true);
        String merchantOrderReference = UUID.randomUUID().toString();
        requestParams.put("amount", amount);
        requestParams.put("currency", "AUD");
        requestParams.put("customerBillingFirstName", customerFirstName);
        requestParams.put("customerBillingLastName", customerLastName);
        requestParams.put("customerBillingAddress1", customerAddressLine1);
        requestParams.put("customerBillingAddress2", customerAddressLine2);
        requestParams.put("customerBillingCity", customerCity);
        requestParams.put("customerBillingPostcode", customerPostcode);
        requestParams.put("customerBillingState", state);
        requestParams.put("customerEmail", emailAddress);
        requestParams.put("sourceIP", ip);
        requestParams.put("merchantId", merchantId);
        requestParams.put("merchantOrderReference", merchantOrderReference);
        requestParams.put("paymentType", "PRE_AUTH");
        requestParams.put("threeDsSource", "ORDER");
        return requestParams;
    }
}
