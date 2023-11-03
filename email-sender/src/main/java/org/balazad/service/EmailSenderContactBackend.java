package org.balazad.service;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.balazad.dto.Contact;

/**
 *
 * @author ador
 */
public class EmailSenderContactBackend implements ContactBackend {

    private static final String MASER_EMAIL_KEY = "MASTER_EMAIL";
    private static final String MASTER_EMAIL_TMPL = "master_email.tmpl";
    private static final String QUESTION_TMPL = "question.tmpl";
    private final Configuration cfg;
    private final String masterEmail;
    private final EmailSenderService emailSenderService;

    public EmailSenderContactBackend() {
        masterEmail = org.balazad.Configuration.INSTANCE.getProperty(MASER_EMAIL_KEY);
        emailSenderService = new EmailSenderService();
        cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setTemplateLoader(new ClassTemplateLoader(EmailSenderContactBackend.class.getClassLoader(), "/templates"));
    }

    @Override
    public void contact(Contact contact) {
        try {
            emailSenderService.sendEmail(masterEmail, String.format("Kérdés '<%s>'", contact.getSenderEmail()), renderMasterEmail(contact));
            emailSenderService.sendEmail(contact.getSenderEmail(), "Köszönjük, hogy felvette velünk a kapcsolatot", renderSenderEmail(contact));
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email!", e);
        }
    }

    private String renderMasterEmail(Contact contact) {
        try {
            Template temp = cfg.getTemplate(MASTER_EMAIL_TMPL);
            StringWriter writer = new StringWriter();
            temp.process(getParameters(contact), writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to render master email", e);
        }
    }

    private String renderSenderEmail(Contact contact) {
        try {
            Template temp = cfg.getTemplate(QUESTION_TMPL);
            StringWriter writer = new StringWriter();
            temp.process(getParameters(contact), writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to render sender email", e);
        }
    }

    private Map<String, String> getParameters(Contact contact) {
        Map<String, String> props = new HashMap<>(4);

        props.put("senderName", contact.getSenderName());
        props.put("senderEmail", contact.getSenderEmail());
        props.put("timestamp", DateTimeFormatter.ISO_DATE_TIME.format(contact.getCreateTime()));
        props.put("content", contact.getText());

        return props;
    }
}
