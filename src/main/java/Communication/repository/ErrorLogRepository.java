package Communication.repository;

import Communication.entity.ErrorLogRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLogRecord, UUID> {
}