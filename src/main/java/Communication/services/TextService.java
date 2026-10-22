package Communication.services;

import Communication.dto.TextRequestDto;
import Communication.dto.UpdateTextBodyDto;
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
public class TextService {

    private static final Logger log = LoggerFactory.getLogger(TextService.class);

    private final MessageRepository messageRepository;
    private final MessageAuditHistoryRepository auditHistoryRepository;

    public TextService(MessageRepository messageRepository,
                       MessageAuditHistoryRepository auditHistoryRepository) {
        this.messageRepository = messageRepository;
        this.auditHistoryRepository = auditHistoryRepository;
    }

    @Transactional
    public MessageRecord processText(TextRequestDto request) {
        log.info("Received SMS text request: {}", request);

        // Subject is null for SMS text messages
        MessageRecord record = new MessageRecord(
                "SMS",
                request.getFrom(),
                request.getTo(),
                request.getSubject(),
                request.getBody(),
                "PENDING"
        );

        MessageRecord saved = messageRepository.save(record);
        log.info("Persisted text metadata with ID: {}", saved.getId());
        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<MessageRecord> retrieveText(UUID id) {
        log.info("Fetching text metadata for ID: {}", id);
        return messageRepository.findById(id);
    }

    @Transactional
    public Optional<MessageRecord> processTextUpdate(UUID id, UpdateTextBodyDto request) {
        log.info("Received text update for ID {}: {}", id, request);

        Optional<MessageRecord> optionalRecord = messageRepository.findById(id);
        if (optionalRecord.isEmpty()) {
            log.warn("Text message with ID {} not found", id);
            return Optional.empty();
        }

        MessageRecord record = optionalRecord.get();
        record.setBody(request.getBody());

        MessageRecord updated = messageRepository.save(record);
        log.info("Successfully updated text message body for ID: {}", id);
        return Optional.of(updated);
    }

    @Transactional
    public boolean deleteText(UUID id) {
        log.info("Attempting to delete and archive text record with ID: {}", id);

        Optional<MessageRecord> optionalRecord = messageRepository.findById(id);
        if (optionalRecord.isEmpty()) {
            log.warn("Delete failed: Text record with ID {} not found", id);
            return false;
        }

        MessageRecord activeMessage = optionalRecord.get();

        // 1. Snapshot active row into audit entity
        MessageAuditHistoryRecord auditRecord = new MessageAuditHistoryRecord(
                activeMessage.getId(),
                activeMessage.getChannelType(),
                activeMessage.getSender(),
                activeMessage.getRecipient(),
                activeMessage.getSubject(),
                activeMessage.getBody(),
                "USER_REQUEST"
        );

        // 2. Persist snapshot to message_audit_history
        auditHistoryRepository.save(auditRecord);
        log.info("Archived text message {} to audit history table", id);

        // 3. Delete row from active messages table
        messageRepository.delete(activeMessage);
        log.info("Successfully deleted text message record with ID: {}", id);
        return true;
    }
}