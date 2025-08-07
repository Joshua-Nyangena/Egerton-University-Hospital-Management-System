package com.eusan.eusanapp.utils;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
public class ThymeleafUtil {

    private static TemplateEngine templateEngine;

    public ThymeleafUtil(TemplateEngine templateEngine) {
        ThymeleafUtil.templateEngine = templateEngine;
    }

    public static String renderTemplate(String templateName, Model model) {
        Context context = new Context();
        
        // Copy model attributes to the Thymeleaf context
        Map<String, Object> attributes = model.asMap();
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }

        return templateEngine.process(templateName, context);
    }
}
