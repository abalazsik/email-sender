package org.balazad.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import java.util.List;
import java.util.Objects;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import org.balazad.Configuration;
import org.glassfish.jersey.client.ClientConfig;

/**
 *
 * @author ador
 */
public class RecaptchaService {

    public static final String CAPTCHA_KEY = "CAPTCHA_KEY";
    public static final String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";
    private final String captchaSiteKey;
    private final Client client;

    public RecaptchaService() {
        captchaSiteKey = Configuration.INSTANCE.getProperty(CAPTCHA_KEY);
        client = ClientBuilder.newClient(new ClientConfig(JacksonJsonProvider.class));
    }

    public void check(String requestToken) throws JsonProcessingException {
        WebTarget target = client.target(RECAPTCHA_URL);

        RecaptchaResponse response = target
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(new Form()
                        .param("secret", captchaSiteKey)
                        .param("response", requestToken)), RecaptchaResponse.class);

        if (!response.success) {
            throw new RuntimeException("recaptcha validation failed! " + Objects.toString(response.errorCodes));
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecaptchaResponse {

        public boolean success;
        public String challenge_ts;
        public String hostname;
        @JsonProperty(value = "error-codes")
        public List<String> errorCodes;
    }

}
