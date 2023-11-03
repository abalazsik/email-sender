package org.balazad.service;

import java.util.logging.Logger;
import org.balazad.dto.Contact;

/**
 *
 * @author ador
 */
public class LoggerContactBackend implements ContactBackend {

    private static final Logger LOGGER = Logger.getLogger(LoggerContactBackend.class.getName());

    @Override
    public void contact(Contact contact) {
        LOGGER.info(String.format("Received contact request from:   <%s>    sender:  \"%s\"  content:    %s", 
                contact.getSenderEmail(), contact.getSenderName(), contact.getText()));
    }

}
