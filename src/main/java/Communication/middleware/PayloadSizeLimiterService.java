package com.example.demo.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class PayloadSizeLimiterService {

    // Example limit: 2 Megabytes (2 * 1024 * 1024 bytes)
    private static final long MAX_ALLOWED_BYTES = 2L * 1024 * 1024;

    /**
     * Checks if the request payload exceeds the allowed size limit.
     *
     * @param request the incoming HTTP request
     * @return true if the payload is within limits, false if it exceeds the limit
     */
    public boolean isPayloadAllowed(HttpServletRequest request) {
        long contentLength = request.getContentLengthLong();

        // 1. If there's no body (like GET/DELETE) or content length is not set, allow it
        /* -->this relates more to HTTP actions, where we are using HTTP actions to perform something on our database
        --
        ---> This is where AUTHORIZATION comes into play becuase think about how can we make sure that thsi request can perform an action, 
        even if our service is GENERAL and they have the right to do, weed need some security checks
                                  */
        if (contentLength <= 0) {
            return true;
        }

        // 2. Check if the declared size exceeds the maximum permitted
        if (contentLength > MAX_ALLOWED_BYTES) {
            return false;
        }

        return true;
    }

    public long getMaxAllowedBytes() {
        return MAX_ALLOWED_BYTES;
    }
}