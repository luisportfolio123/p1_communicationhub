package com.example.demo.filter;

import com.example.demo.service.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component //bean declaraion 
@Order(1) //Ensures this runs at the very beginning fo the filter chain
public class GlobalRequestFilter extends OncePerRequestFilter{

    //reference rate limiter middleware
    private final RateLimiterService rateLimiterService;

    //Gemini Comment: Spring injects RateLimiterService automatically - Comment: What does this exactly do?
    public GlobalRequestFilter(RateLimiterService rateLimiterService){//remember we are dealing with beans so we are referencing the rateLmierService bean
        this.rateLimiterService = rateLimiterService;
    }

    @Override //Comment: Function needs clarificaiton
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, Filterchain filterchain) throws ServletException, IOException {
        String requestIp = extractClientIp(request);

        //Rate Limiter
        if(!rateLimiterService.tryConsume(requestIp)){//threw me a false
            response.setStatus(HttpStatus.TOO_MANY_REQUEST.value());
            response.setContentType("application/json");
            response.setHeader("Retry-After", "60");
            response.getWriter().write("{\"error\": \"Too many requests. Please try again later.\"}");
            return;
        }

        //2. Add another global middleware checks here (Helmet, request ID etc)

        //3. Continue downstream to controllers
        filterChain.doFilter(request, response);
    }
}















/*
Future useful code - ex, cloudfare
private String extractClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

 */