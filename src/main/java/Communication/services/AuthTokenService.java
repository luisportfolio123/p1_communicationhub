package Communication.services;

import Communication.entity.ApiTokenRecord;
import Communication.repository.ApiTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class AuthTokenService {

    private final ApiTokenRepository tokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public AuthTokenService(ApiTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public ApiTokenRecord generateToken(String clientName, Duration validityDuration) {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        Instant expiresAt = Instant.now().plus(validityDuration);
        ApiTokenRecord record = new ApiTokenRecord(rawToken, clientName, expiresAt);

        return tokenRepository.save(record);
    }

    @Transactional(readOnly = true)
    public boolean validateToken(String tokenValue) {
        if (tokenValue == null || tokenValue.isBlank()) {
            return false;
        }

        Optional<ApiTokenRecord> tokenRecord = tokenRepository.findByTokenValue(tokenValue);
        return tokenRecord.isPresent() && tokenRecord.get().isValid();
    }
}