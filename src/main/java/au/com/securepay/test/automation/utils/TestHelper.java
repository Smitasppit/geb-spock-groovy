package au.com.securepay.test.automation.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TestHelper {

    public static String getMessageTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return new SimpleDateFormat("yyyyMMddhhmmssSSS000Z").format(timestamp);
    }
}
