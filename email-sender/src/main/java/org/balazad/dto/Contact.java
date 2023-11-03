package org.balazad.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Contact details
 * @author ador
 */
public class Contact implements Serializable {

    private String senderName;
    private String senderEmail;
    private String text;
    private String token;
    private LocalDateTime createTime;

    public Contact() {
    }
    
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public void validate() {
        Objects.requireNonNull(senderName, "senderName must be notNull!!");
        Objects.requireNonNull(senderEmail, "senderEmail must be notNull!!");
        Objects.requireNonNull(text, "text must be notNull!!");
        Objects.requireNonNull(token, "token must be notNull!!");
    }
    
}
