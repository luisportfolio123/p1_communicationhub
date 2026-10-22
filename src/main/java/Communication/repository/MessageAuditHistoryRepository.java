package Communication.repository;

import Communication.entity.MessageAuditHistoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageAuditHistoryRepository extends JpaRepository<MessageAuditHistoryRecord, UUID> {
}