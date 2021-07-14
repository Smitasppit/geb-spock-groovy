package au.com.securepay.test.automation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HTTPUtils {
    private static final Logger logger = LoggerFactory.getLogger(HTTPUtils.class);

    public static Map<String, String> getRequestParams(String request) {
        try {
            String decodedRequest = URLDecoder.decode(request, StandardCharsets.UTF_8.name());
            String[] pairs = decodedRequest.split("&");
            // no param
            if (pairs.length == 0) return null;
            Map<String, String> requestParams = new HashMap<>();
            for (String pair : pairs) {
                String[] fields = pair.split("=");
                // key & value
                if (fields.length == 2) {
                    requestParams.put(fields[0], fields[1]);
                }
            }
            return requestParams;
        } catch (UnsupportedEncodingException e) {
            logger.info("Exception: ", e);
            return null;
        }
    }
}
