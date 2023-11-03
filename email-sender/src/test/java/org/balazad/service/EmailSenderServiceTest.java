package org.balazad.service;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author ador
 */
public class EmailSenderServiceTest {

    @Test
    public void testGenerateSenderEmail() {
        Assert.assertEquals("noreply@manonacid.hu", EmailSenderService.generateSenderEmail("noreply", "mail.manonacid.hu"));
    }

}
