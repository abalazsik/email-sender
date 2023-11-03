package org.balazad;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.balazad.dto.Contact;
import org.balazad.service.EmailSenderContactBackend;
import org.balazad.service.ContactBackend;
import org.balazad.service.RecaptchaService;

/**
 *
 * @author ador
 */
public class ContactSenderLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ContactBackend contactService = new EmailSenderContactBackend();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, String> CORS_HEADERS = new HashMap<>(4) {
        {
            this.put("Access-Control-Allow-Origin", "*");
            this.put("Access-Control-Allow-Credentials", "true");
            this.put("Access-Control-Allow-Methods", "OPTIONS,POST");
            this.put("Access-Control-Allow-Headers", "Content-Type");
        }
    };
    
    private final RecaptchaService recaptchaService = new RecaptchaService();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context cntxt) {
        try {
            if ("OPTIONS".equals(event.getHttpMethod())) {
                return new APIGatewayProxyResponseEvent()
                        .withHeaders(CORS_HEADERS)
                        .withStatusCode(200);
            }

            Contact contact = objectMapper.readValue(event.getBody(), Contact.class);

            contact.validate();
            contact.setCreateTime(LocalDateTime.now());
            
            recaptchaService.check(contact.getToken());

            contactService.contact(contact);

            return new APIGatewayProxyResponseEvent()
                    .withHeaders(CORS_HEADERS)
                    .withStatusCode(200);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500);
        }

    }

}
