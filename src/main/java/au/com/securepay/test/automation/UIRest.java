package au.com.securepay.test.automation;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
/*
 * This is used to store the message exchange between UI(Browser) and Server.
 */
public class UIRest {
    HttpRequest request;
    String requestContents;
    HttpResponse response;
    String responseContents;

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public String getRequestContents() {
        return requestContents;
    }

    public void setRequestContents(String requestContents) {
        this.requestContents = requestContents;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public String getResponseContents() {
        return responseContents;
    }

    public void setResponseContents(String responseContents) {
        this.responseContents = responseContents;
    }
}
