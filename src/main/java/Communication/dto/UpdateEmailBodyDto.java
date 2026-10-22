package Communication.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateEmailBodyDto {

    @NotBlank(message = "Email body cannot be empty")
    private String body;

    public UpdateEmailBodyDto() {}

    public UpdateEmailBodyDto(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}