package com.example.demo.service;//--> Replaced to match our own evironment

import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service//bean declaration
public class RequestIdService {
    private static final String MDC_KEY = "requestId";

    public static final String RESPONSE_HEADER_NAME = "X-Request-Id";
    public static final String REQUEST_ATTRIBUTE_KEY = "internalRequestId";

    public String generateRequestId(){
        String requestId = UUID.randomUUID().toString();
        return requestId;
    }

    // Attaches the new ID to the request object so downstream code/controllers can read it
    public void attachIdToHTTPRequest(HttpServletRequest request, String requestId){
        /*- Ok so if this function was to get called in a real production by a junior engineer, sould there not be some checks even if it in our code, that
        hey who ever make this call from what every location in order to sucessfully execute this function we are strict and want to ensure you handed us the correct information - typescript
         */
        request.setAttribute(REQUEST_ATTRIBUTE_KEY, requestId);
    }

    //---> this is concepts related more to Spring
    // Binds our ID to the logging engine
    public void bindToLoggingContext(String requestId) {
        MDC.put(MDC_KEY, requestId);
    }

    // Cleans up the worker thread
    public void clearLoggingContext() {
        MDC.remove(MDC_KEY);
    }
}

