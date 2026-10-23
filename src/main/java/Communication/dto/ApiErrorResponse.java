package Communication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private String requestId;
    private Map<String, String> validationErrors;

    public ApiErrorResponse() {
        this.timestamp = Instant.now();
    }

    public ApiErrorResponse(int status, String error, String message, String path) {
        this.timestamp = Instant.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ApiErrorResponse(int status, String error, String message, String path, Map<String, String> validationErrors) {
        this(status, error, message, path);
        this.validationErrors = validationErrors;
    }

    // Factory methods matching GlobalExceptionHandler calls
    public static ApiErrorResponse of(int status, String error, String message, String path) {
        return new ApiErrorResponse(status, error, message, path);
    }

    public static ApiErrorResponse of(int status, String error, String message, String path, String requestId) {
        ApiErrorResponse response = new ApiErrorResponse(status, error, message, path);
        response.setRequestId(requestId);
        return response;
    }

    public static ApiErrorResponse withValidation(int status, String error, String message, String path, Map<String, String> validationErrors) {
        return new ApiErrorResponse(status, error, message, path, validationErrors);
    }

    public static ApiErrorResponse withValidation(int status, String error, String message, String path, String requestId, Map<String, String> validationErrors) {
        ApiErrorResponse response = new ApiErrorResponse(status, error, message, path, validationErrors);
        response.setRequestId(requestId);
        return response;
    }

    // Getters and Setters
    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}