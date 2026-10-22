package Communication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TextRequestDto {

    // Matches standard US formats: +1XXXXXXXXXX, 1XXXXXXXXXX, (XXX) XXX-XXXX, XXX-XXX-XXXX, or 10 digits
    private static final String US_PHONE_REGEX = "^(\\+1[-.\\s]?)?(\\(?[2-9]\\d{2}\\)?[-.\\s]?)?[2-9]\\d{2}[-.\\s]?\\d{4}$";

    @NotBlank(message = "Recipient ('to') phone number is required")
    @Pattern(regexp = US_PHONE_REGEX, message = "Recipient ('to') must be a valid US phone number")
    private String to;

    @NotBlank(message = "Sender ('from') phone number is required")
    @Pattern(regexp = US_PHONE_REGEX, message = "Sender ('from') must be a valid US phone number")
    private String from;

    @NotBlank(message = "Text message body cannot be empty")
    @Size(max = 1600, message = "Text message exceeds maximum character limit of 1600 characters")
    private String body;

    public TextRequestDto() {}

    public TextRequestDto(String to, String from, String body) {
        this.to = to != null ? to.trim() : null;
        this.from = from != null ? from.trim() : null;
        this.body = body;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to != null ? to.trim() : null;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from != null ? from.trim() : null;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "TextRequestDto{" +
                "to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", bodyLength=" + (body != null ? body.length() : 0) +
                '}';
    }
}