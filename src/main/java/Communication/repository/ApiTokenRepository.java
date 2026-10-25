package Communication.repository;

import Communication.entity.ApiTokenRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApiTokenRepository extends JpaRepository<ApiTokenRecord, UUID> {
    Optional<ApiTokenRecord> findByTokenValue(String tokenValue);
}