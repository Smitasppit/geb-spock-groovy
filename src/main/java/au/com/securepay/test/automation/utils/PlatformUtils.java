package au.com.securepay.test.automation.utils;

import org.apache.commons.lang3.StringUtils;

public class PlatformUtils {
    private static String SP_XML_SYNTAX = "SecurePayMessage";
    private static String NT_XML_SYNTAX = "NABTransactMessage";

    public static String getXMLSyntax() {
        String platform = StringUtils.trimToNull(System.getProperty("automation.platform"));
        if ((platform != null) && platform.equalsIgnoreCase("NT")) {
            return NT_XML_SYNTAX;
        } else {
            return SP_XML_SYNTAX;
        }
    }
}
