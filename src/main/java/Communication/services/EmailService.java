package Communication.services;

import Communication.dto.EmailRequestDto;
import Communication.dto.UpdateEmailBodyDto;
import Communication.entity.MessageAuditHistoryRecord;
import Communication.entity.MessageRecord;
import Communication.repository.MessageAuditHistoryRepository;
import Communication.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final MessageRepository messageRepository;
    private final MessageAuditHistoryRepository auditHistoryRepository;

    public EmailService(MessageRepository messageRepository,
                        MessageAuditHistoryRepository auditHistoryRepository) {
        this.messageRepository = messageRepository;
        this.auditHistoryRepository = auditHistoryRepository;
    }

    @Transactional
    public MessageRecord processEmail(EmailRequestDto request) {
        log.info("Received email request: {}", request);

        MessageRecord record = new MessageRecord(
                "EMAIL",
                request.getFrom(),
                request.getTo(),
                request.getSubject(),
                request.getBody(),
                "PENDING"
        );

        MessageRecord saved = messageRepository.save(record);
        log.info("Persisted email with ID: {}", saved.getId());
        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<MessageRecord> retrieveEmail(UUID id) {
        log.info("Fetching email with ID: {}", id);
        return messageRepository.findById(id);
    }

    @Transactional
    public Optional<MessageRecord> processEmailUpdate(UUID id, UpdateEmailBodyDto request) {
        log.info("Received update for ID {}: {}", id, request);

        Optional<MessageRecord> optionalRecord = messageRepository.findById(id);
        if (optionalRecord.isEmpty()) {
            log.warn("Message with ID {} not found", id);
            return Optional.empty();
        }

        MessageRecord record = optionalRecord.get();
        record.setBody(request.getBody());

        MessageRecord updated = messageRepository.save(record);
        log.info("Updated message body for ID: {}", id);
        return Optional.of(updated);
    }

    @Transactional
    public boolean deleteEmail(UUID id) {
        log.info("Attempting to delete and archive email record with ID: {}", id);

        Optional<MessageRecord> optionalRecord = messageRepository.findById(id);
        if (optionalRecord.isEmpty()) {
            log.warn("Delete failed: record {} not found", id);
            return false;
        }

        MessageRecord activeMessage = optionalRecord.get();

        // 1. Create audit snapshot from active message
        MessageAuditHistoryRecord auditRecord = new MessageAuditHistoryRecord(
                activeMessage.getId(),
                activeMessage.getChannelType(),
                activeMessage.getSender(),
                activeMessage.getRecipient(),
                activeMessage.getSubject(),
                activeMessage.getBody(),
                "USER_REQUEST"
        );

        // 2. Persist to message_audit_history table
        auditHistoryRepository.save(auditRecord);
        log.info("Archived message {} to audit history table", id);

        // 3. Remove from active messages table
        messageRepository.delete(activeMessage);
        log.info("Successfully deleted message record with ID: {}", id);
        return true;
    }
}