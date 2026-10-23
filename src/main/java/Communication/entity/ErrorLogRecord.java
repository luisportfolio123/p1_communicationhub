package Communication.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "error_logs")
public class ErrorLogRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "error_id", updatable = false, nullable = false, columnDefinition = "char(36)")
    private UUID errorId;

    @Column(name = "request_id", nullable = false, updatable = false)
    private String requestId;

    @Column(name = "http_method", nullable = false, updatable = false)
    private String httpMethod;

    @Column(name = "endpoint", nullable = false, updatable = false)
    private String endpoint;

    @Column(name = "error_type", nullable = false, updatable = false)
    private String errorType;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false, updatable = false)
    private String message;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public ErrorLogRecord() {}

    public ErrorLogRecord(String requestId, String httpMethod, String endpoint, String errorType, String message) {
        this.requestId = requestId;
        this.httpMethod = httpMethod;
        this.endpoint = endpoint;
        this.errorType = errorType;
        this.message = message;
        this.createdAt = Instant.now();
    }

    public UUID getErrorId() { return errorId; }
    public String getRequestId() { return requestId; }
    public String getHttpMethod() { return httpMethod; }
    public String getEndpoint() { return endpoint; }
    public String getErrorType() { return errorType; }
    public String getMessage() { return message; }
    public Instant getCreatedAt() { return createdAt; }
}