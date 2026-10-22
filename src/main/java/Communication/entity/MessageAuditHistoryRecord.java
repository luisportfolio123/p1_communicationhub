package Communication.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "message_audit_history")
public class MessageAuditHistoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "history_id", updatable = false, nullable = false)
    private UUID historyId;

    @Column(name = "original_message_id", nullable = false, updatable = false)
    private UUID originalMessageId;

    @Column(name = "channel_type", nullable = false, updatable = false)
    private String channelType;

    @Column(name = "sender", nullable = false, updatable = false)
    private String sender;

    @Column(name = "recipient", nullable = false, updatable = false)
    private String recipient;

    @Column(name = "subject", updatable = false)
    private String subject;

    @Column(name = "body", columnDefinition = "TEXT", nullable = false, updatable = false)
    private String body;

    @Column(name = "deleted_at", nullable = false, updatable = false)
    private Instant deletedAt;

    @Column(name = "deleted_reason", updatable = false)
    private String deletedReason;

    public MessageAuditHistoryRecord() {}

    public MessageAuditHistoryRecord(UUID originalMessageId, String channelType, String sender, String recipient, String subject, String body, String deletedReason) {
        this.originalMessageId = originalMessageId;
        this.channelType = channelType;
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.deletedReason = deletedReason != null ? deletedReason : "USER_REQUEST";
    }

    @PrePersist
    protected void onCreate() {
        if (this.deletedAt == null) {
            this.deletedAt = Instant.now();
        }
        if (this.deletedReason == null) {
            this.deletedReason = "USER_REQUEST";
        }
    }

    public UUID getHistoryId() {
        return historyId;
    }

    public void setHistoryId(UUID historyId) {
        this.historyId = historyId;
    }

    public UUID getOriginalMessageId() {
        return originalMessageId;
    }

    public void setOriginalMessageId(UUID originalMessageId) {
        this.originalMessageId = originalMessageId;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDeletedReason() {
        return deletedReason;
    }

    public void setDeletedReason(String deletedReason) {
        this.deletedReason = deletedReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageAuditHistoryRecord that)) return false;
        return Objects.equals(historyId, that.historyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyId);
    }
}