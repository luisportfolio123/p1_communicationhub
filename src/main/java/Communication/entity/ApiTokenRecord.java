package Communication.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "api_tokens")
public class ApiTokenRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "char(36)")
    private UUID id;

    @Column(name = "token_value", nullable = false, unique = true)
    private String tokenValue;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public ApiTokenRecord() {}

    public ApiTokenRecord(String tokenValue, String clientName, Instant expiresAt) {
        this.tokenValue = tokenValue;
        this.clientName = clientName;
        this.active = true;
        this.expiresAt = expiresAt;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getTokenValue() { return tokenValue; }
    public String getClientName() { return clientName; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getCreatedAt() { return createdAt; }

    public boolean isValid() {
        return this.active && this.expiresAt.isAfter(Instant.now());
    }
}