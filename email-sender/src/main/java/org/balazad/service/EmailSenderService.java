package org.balazad.service;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.balazad.Configuration;

/**
 *
 * @author ador
 */
public class EmailSenderService {

    private static final String SMTP_SERVER_KEY = "SMTP_SERVER_KEY";
    private static final String SMTP_SERVER_PORT_KEY = "SMTP_SERVER_PORT_KEY";

    private static final String SMTP_USER_KEY = "SMTP_USER_KEY";

    private static final String SMTP_PASSWORD_KEY = "SMTP_PASSWORD_KEY";

    private static final Logger LOGGER = Logger.getLogger(EmailSenderContactBackend.class.getName());

    private final Session mailSession;
    private final String senderEmail;

    public EmailSenderService() {
        Properties props = new Properties();

        props.put("mail.smtp.host", Configuration.INSTANCE.getProperty(SMTP_SERVER_KEY));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.user", Configuration.INSTANCE.getProperty(SMTP_USER_KEY));
        props.put("mail.smtp.password", Configuration.INSTANCE.getProperty(SMTP_PASSWORD_KEY));
        props.put("mail.debug", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", Configuration.INSTANCE.getProperty(SMTP_SERVER_PORT_KEY));
        props.put("mail.smtp.socketFactory.port", Configuration.INSTANCE.getProperty(SMTP_SERVER_PORT_KEY));
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.allow8bitmime", "true");
        props.setProperty("mail.smtps.allow8bitmime", "true");

        senderEmail = generateSenderEmail(Configuration.INSTANCE.getProperty(SMTP_USER_KEY), Configuration.INSTANCE.getProperty(SMTP_SERVER_KEY));

        mailSession = Session.getInstance(props, new javax.mail.Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, Configuration.INSTANCE.getProperty(SMTP_PASSWORD_KEY));
            }
        });
    }

    static String generateSenderEmail(String smtpUser, String smtpServer) {
        if (!smtpServer.startsWith("mail.")) {
            throw new IllegalArgumentException("smtpServer should start with 'mail.'!");
        }
        return smtpUser + "@" + smtpServer.substring("mail.".length());
    }

    public void sendEmail(String email, String title, String content) throws AddressException, MessagingException {
        Message msg = new MimeMessage(mailSession);

        msg.setFrom(new InternetAddress(senderEmail));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        msg.setSentDate(new Date());
        msg.setSubject(title);

        msg.setContent(content, "text/html");

        Transport.send(msg);
        LOGGER.log(Level.INFO, "Email sent to {0}", email);
    }

}
