package com.example.demo.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service//bean declaration
public class RateLimiterService {

    //Thread-safe map to store token buckets mapped to an IP address (string value)
    private final Map<String, Bucket> ipTrackerDS = new ConcurrentHashMap<>();

    private Bucket createIpTracker() {
        Refill refill = Refill.intervally(10, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(10, refill);

        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket getIpTracker(String ipAddress){
        //Look up if there already exist an ip tracker for the ip address
        Bucket ipTracker = ipTrackerDS.get(ipAddress);

        //If no tracker exist, create one an append it onto our map 'ipTrackerDS'
        if (ipTracker == null) {
            ipTracker = createIpTracker();
            ipTrackerDS.put(ipAddress, ipTracker);
        }
        return ipTracker;
    }

    public boolean consumeRequest(String ipAddress){
        Bucket ipTracker = getIpTracker(ipAddress);
        boolean consumed = ipTracker.tryConsume(1);
        return consumed;
    }

}
























/*Comments made by Gemini that need some clarification

resolveBucket()
    *Resolves an existing bucket for an IP or initializes a new one.
    *Configuration: 10 tokens max, refills 10 tokens every 1 minute.

tryConsumer()
    *Attempts to consume 1 token for the specified IP.
    *@return true if permitted, false if rate limit exceeded.


* */


/*Personal Notes
Bucket comes from the bucket4j library (a rabbit hole)
 */