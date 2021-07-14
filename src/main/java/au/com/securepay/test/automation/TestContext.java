package au.com.securepay.test.automation;

import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright(c) 2019 SecurePay Pty Ltd. All rights reserved by SecurePay Pty Ltd.
 * Container used to share data between multiple test step classes
 */
public class TestContext {
    private Map<String, Object> requestParams;
    private ValidatableResponse response;
    private List<UIRest> uiRestList;
    private UIRest currentUIRest;

    public TestContext() {
        requestParams = new HashMap<>();
        uiRestList = new ArrayList<>();
    }

    public Map<String, Object> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(Map<String, Object> requestParams) {
        this.requestParams = requestParams;
    }

    public ValidatableResponse getResponse() {
        return response;
    }


    public void setResponse(ValidatableResponse response) {
        this.response = response;
    }

    /**
     * Add a request param.
     * @param key
     * @param value
     */
    public void putRequestParam(String key, String value) {
        requestParams.put(key, value);
    }

    /**
     * Resolve a test data value based on the given data key and set it as a request param against the given key.
     * @param key
     * @param dataKey
     * @throws Throwable
     */
    public void resolveRequestParam(String key, String dataKey) throws Throwable {
        putRequestParam(key, TestDataManager.getInstance().getData(dataKey));
    }

    public UIRest getCurrentUIRest(){
        return currentUIRest;
    }

    public void setCurrentUIRest(UIRest uiRest){
        currentUIRest = uiRest;
    }

    public void addUIRest(UIRest uiRest){
        uiRestList.add(uiRest);
    }

    public List<UIRest> getUIRestList(){
        return uiRestList;
    }
}
