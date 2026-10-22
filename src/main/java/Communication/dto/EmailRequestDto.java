package Communication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailRequestDto {

    @NotBlank(message = "'to' address cannot be empty")
    @Email(message = "Invalid 'to' email address format")
    private String to;

    @NotBlank(message = "'from' address cannot be empty")
    @Email(message = "Invalid 'from' email address format")
    private String from;

    private String subject;

    @NotBlank(message = "Email body cannot be empty")
    private String body;

    public EmailRequestDto() {}

    public EmailRequestDto(String to, String from, String subject, String body) {
        this.to = to != null ? to.trim() : null;
        this.from = from != null ? from.trim() : null;
        this.subject = subject;
        this.body = body;
    }

    public void setTo(String to) {
        this.to = to != null ? to.trim() : null;
    }

    public String getTo() {
        return to;
    }

    public void setFrom(String from) {
        this.from = from != null ? from.trim() : null;
    }

    public String getFrom() {
        return from;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "EmailRequestDto{" +
                "to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", subject='" + subject + '\'' +
                ", bodyLength=" + (body != null ? body.length() : 0) +
                '}';
    }
}