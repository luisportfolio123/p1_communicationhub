package Communication.services;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    // Capacity: 20 requests per minute per IP address
    private static final int CAPACITY = 20;
    private static final Duration REFILL_DURATION = Duration.ofMinutes(1);

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        Refill refill = Refill.intervally(CAPACITY, REFILL_DURATION);
        Bandwidth limit = Bandwidth.classic(CAPACITY, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    public boolean tryConsume(String ipAddress) {
        // computeIfAbsent is atomic, preventing race conditions under concurrent hits
        Bucket bucket = buckets.computeIfAbsent(ipAddress, key -> createNewBucket());
        return bucket.tryConsume(1);
    }
}