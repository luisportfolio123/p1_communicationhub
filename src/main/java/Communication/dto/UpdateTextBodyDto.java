package Communication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateTextBodyDto {

    @NotBlank(message = "Text message body cannot be empty")
    @Size(max = 1600, message = "Text message exceeds maximum character limit of 1600 characters")
    private String body;

    public UpdateTextBodyDto() {}

    public UpdateTextBodyDto(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}