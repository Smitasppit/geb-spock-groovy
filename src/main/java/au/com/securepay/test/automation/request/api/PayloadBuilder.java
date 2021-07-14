package au.com.securepay.test.automation.request.api;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright(c) 2019 SecurePay Pty Ltd. All rights reserved by SecurePay Pty Ltd.
 * Loads relevant request template and generates payload by applying request context
 */
public class PayloadBuilder {
    private static Logger logger = LoggerFactory.getLogger(PayloadBuilder.class);
    private static PayloadBuilder payloadBuilder = null;
    private Map<String, String> rawTemplates = new HashMap<>();


    private PayloadBuilder() throws IOException {
        Collection<File> templates = FileUtils.listFiles(new File(getClass().getResource("/request-templates/").getFile()),
                new String[]{"hbs"}, false);
        for (File template: templates) {
            String key = FilenameUtils.removeExtension(template.getName());
            logger.info("Loading request template {}", key);
            rawTemplates.put(key, FileUtils.readFileToString(template, StandardCharsets.UTF_8));
        }
    }

    private void loadTemplate(String type) throws IOException {
        String path = getClass().getResource("/request-templates/" + type + ".hbs").getFile();

    }

    public static PayloadBuilder getInstance() throws IOException {
        if (payloadBuilder == null) {
            payloadBuilder = new PayloadBuilder();
        }
        return payloadBuilder;
    }

    /**
     * Render a request payload based on the given template (which will be loaded from src/test/resources/request-templates/)
     * and the data in the given model.
     *
     * @param model
     * @param templateName
     * @return Rendered Payload
     * @throws IOException
     */
    public String getPayload(Map<String, Object> model, String templateName) throws IOException {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline(rawTemplates.get(templateName));
        return template.apply(model);
    }
}
